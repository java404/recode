package smartmon.core.agent.service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.agent.AgentStateEnum;
import smartmon.core.agent.SmartMonBatchConfig;
import smartmon.core.config.SmartMonErrno;
import smartmon.core.hosts.mapper.SmartMonHostMapper;
import smartmon.core.hosts.SmartMonHost;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.utilities.general.SmartMonException;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.ssh.SecureCopy;
import smartmon.utilities.ssh.SecureCopyEvent;
import smartmon.utilities.ssh.SecureCopyParameters;
import smartmon.utilities.ssh.ShellExecute;
import smartmon.utilities.ssh.ShellExecuteEvent;

@Service
public class AgentUninstallService {
  @Autowired
  private SmartMonBatchConfig smartMonBatchConfig;
  @Autowired
  private SmartMonHostMapper smartMonHostMapper;
  @Autowired
  private TaskManagerService taskManagerService;
  @Autowired
  private AgentStateService agentStateService;

  public TaskGroup uninstallAgent(String hostUuid) {
    SmartMonHost smartMonHost = smartMonHostMapper.selectByPrimaryKey(hostUuid);
    checkAgentStateForUninstall(smartMonHost.getManageIp(), smartMonHost.getAgentState());
    TargetHost targetHost = TargetHost.builder(smartMonHost.getManageIp(), smartMonHost.getSshPort())
      .username(smartMonHost.getSysUsername()).password(smartMonHost.getSysPassword()).build();
    Map<String, String> parameters = Maps.newHashMap();
    parameters.put("hostUuid", hostUuid);
    parameters.put("hostAddress", smartMonHost.getManageIp());
    TaskDescription desc = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_UNINSTALL).withResource(TaskRes.RES_AGENT).withParameters(parameters)
      .withStep("UNINSTALL", "uninstall agent", () -> uninstallAgent(targetHost))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("UninstallAgent", desc);
    taskManagerService.invokeTaskGroup(taskGroup);
    agentStateService.setAgentUninstallingState(smartMonHost, taskGroup.getTaskGroupId().toString());
    return taskGroup;
  }

  private void checkAgentStateForUninstall(String address, AgentStateEnum agentState) {
    if (agentState == null
      || agentState == AgentStateEnum.INSTALLING
      || agentState == AgentStateEnum.UNINSTALLING) {
      String message = String.format("Agent state error, can not uninstall agent on host[%s] at state[%s]",
        address, agentState);
      throw new SmartMonException(SmartMonErrno.AGENT_STATE_ERROR, message);
    }
  }

  private void uninstallAgent(TargetHost targetHost) {
    try {
      TaskContext currentContext = TaskContext.currentTaskContext();
      SecureCopyEvent secureCopyEvent = (
        SecureCopyParameters options, String source, String target, long sourceSize, long count, boolean ended) -> {
        String message = String.format("From: %s, to: %s (%d/%d)", source, target, count, sourceSize);
        currentContext.getCurrentStep().updateLog(message);
      };
      copyScripts(targetHost, secureCopyEvent);
      ShellExecuteEvent shellExecuteEvent = (message) -> {
        currentContext.getCurrentStep().appendLog(message);
      };
      uninstallAgent(targetHost, shellExecuteEvent);
    } finally {
      smartMonBatchConfig.deleteTempDir(targetHost);
    }
  }

  private void copyScripts(TargetHost targetHost, SecureCopyEvent secureCopyEvent) {
    final SecureCopyParameters parameters = new SecureCopyParameters();
    parameters.setSourceRoot(smartMonBatchConfig.getSourceRoot());
    final Set<String> sources = Sets.newHashSet();
    sources.add("scripts/agent_uninstall.sh");
    parameters.setSourceFile(sources);
    parameters.setTargetFolder(smartMonBatchConfig.getWorkFolder());
    parameters.setRecreateTargetFolder(true);
    final SecureCopy scp = new SecureCopy(targetHost);
    scp.setCopyEvent(secureCopyEvent);
    try {
      scp.copy(parameters);
    } catch (Exception err) {
      throw new SmartMonException(SmartMonErrno.SSH_COPY_FILE_ERROR, err.getMessage());
    }
  }

  private void uninstallAgent(TargetHost targetHost, ShellExecuteEvent shellExecuteEvent) {
    int code = 0;
    try {
      ShellExecute shellExecutor = new ShellExecute(targetHost);
      shellExecutor.setExecuteEvent(shellExecuteEvent);
      String command = String.format("cd %s/scripts && ./agent_uninstall.sh 2>&1",
        smartMonBatchConfig.getWorkFolder());
      code = shellExecutor.run(command);
    } catch (Exception err) {
      throw new SmartMonException(SmartMonErrno.SSH_COMMAND_ERROR, err.getMessage());
    }
    if (!Objects.equals(0, code)) {
      throw new SmartMonException(SmartMonErrno.SSH_COMMAND_ERROR, "uninstall agent error");
    }
  }
}
