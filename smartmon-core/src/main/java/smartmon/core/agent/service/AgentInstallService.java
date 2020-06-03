package smartmon.core.agent.service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.agent.AgentStateEnum;
import smartmon.core.agent.SmartMonBatchConfig;
import smartmon.core.config.SmartMonErrno;
import smartmon.core.hosts.HostsService;
import smartmon.core.hosts.mapper.SmartMonHostMapper;
import smartmon.core.hosts.types.SmartMonHost;
import smartmon.core.repo.RepoService;
import smartmon.injector.client.AgentClientService;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.misc.TaskSynchronizer;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonException;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.ssh.SecureCopy;
import smartmon.utilities.ssh.SecureCopyEvent;
import smartmon.utilities.ssh.SecureCopyParameters;
import smartmon.utilities.ssh.ShellExecute;
import smartmon.utilities.ssh.ShellExecuteEvent;
import smartmon.webdata.config.SystemConfig;

@Slf4j
@Service
public class AgentInstallService {
  @Autowired
  private SmartMonBatchConfig smartMonBatchConfig;
  @Autowired
  private SystemConfig systemConfig;
  @Autowired
  private SmartMonHostMapper smartMonHostMapper;
  @Autowired
  private HostsService hostService;
  @Autowired
  private TaskManagerService taskManagerService;
  @Autowired
  private RepoService repoService;
  @Autowired
  private AgentClientService agentClientService;
  @Autowired
  private AgentStateService agentStateService;

  public synchronized TaskGroup installBasicService(TargetHost targetHost) {
    Runnable configRepoClient = () -> repoService.configRepoClient(targetHost);
    Runnable installBasicService = () -> installInjector(targetHost);
    Map<String, String> parameters = Maps.newHashMap();
    parameters.put("hostAddress", targetHost.getAddress());
    TaskDescription desc = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_INSTALL).withResource(TaskRes.RES_INJECTOR).withParameters(parameters)
      .withStep("INSTALL", "config yum repo", configRepoClient)
      .withStep("INSTALL", "install basic service", installBasicService)
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("InstallInjector", desc);
    taskManagerService.invokeTaskGroup(taskGroup);
    return taskGroup;
  }

  public synchronized TaskGroup installAgent(String hostUuid) {
    SmartMonHost smartMonHost = smartMonHostMapper.selectByPrimaryKey(hostUuid);
    checkAgentState(smartMonHost.getManageIp(), smartMonHost.getAgentState());
    TargetHost targetHost = TargetHost.builder(smartMonHost.getManageIp(), smartMonHost.getSshPort())
      .username(smartMonHost.getSysUsername()).password(smartMonHost.getSysPassword()).build();
    Runnable configRepoClient = () -> repoService.configRepoClient(targetHost);
    Runnable installBasicService = () -> installInjector(targetHost);
    Runnable installAgentService = () -> installAgentService(smartMonHost.getManageIp(), hostUuid);
    Map<String, String> parameters = Maps.newHashMap();
    parameters.put("hostUuid", hostUuid);
    parameters.put("hostAddress", smartMonHost.getManageIp());
    TaskDescription desc = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_INSTALL).withResource(TaskRes.RES_AGENT).withParameters(parameters)
      .withStep("INSTALL", "config yum repo", configRepoClient)
      .withStep("INSTALL", "install basic service", installBasicService)
      .withStep("INSTALL", "install agent service", installAgentService)
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("InstallAgent", desc);
    taskManagerService.invokeTaskGroup(taskGroup);
    agentStateService.setAgentInstallingState(smartMonHost, taskGroup.getTaskGroupId().toString());
    return taskGroup;
  }

  private void checkAgentState(String address, AgentStateEnum agentState) {
    if (agentState != null
      && agentState != AgentStateEnum.UNINSTALL_SUCCESS
      && agentState != AgentStateEnum.INSTALL_FAILED) {
      String message = String.format("Agent state error, can not install agent on host[%s] at state[%s]",
        address, agentState);
      throw new SmartMonException(SmartMonErrno.AGENT_STATE_ERROR, message);
    }
  }

  public void installInjectorIfNeed(TargetHost targetHost) {
    try {
      Boolean isInjectorHealthy = agentClientService.isInjectorHealthy(targetHost.getAddress());
      if (Objects.equals(Boolean.TRUE, isInjectorHealthy)) {
        TaskContext.currentTaskContext().getCurrentStep().appendLog("injector already installed");
        return;
      }
    } catch (Exception ignored) {
    }
    repoService.configRepoClient(targetHost);
    installInjector(targetHost);
  }

  private void installInjector(TargetHost targetHost) {
    try {
      TaskContext currentContext = TaskContext.currentTaskContext();
      SecureCopyEvent secureCopyEvent = (
        SecureCopyParameters options, String source, String target, long sourceSize, long count, boolean ended) -> {
        String message = String.format("From: %s, to: %s (%d/%d)", source, target, count, sourceSize);
        currentContext.getCurrentStep().appendLog(message);
      };
      copyScripts(targetHost, secureCopyEvent);
      ShellExecuteEvent shellExecuteEvent = (message) -> {
        currentContext.getCurrentStep().appendLog(message);
      };
      installInjector(targetHost, shellExecuteEvent);
    } finally {
      smartMonBatchConfig.deleteTempDir(targetHost);
    }
  }

  private void copyScripts(TargetHost targetHost, SecureCopyEvent secureCopyEvent) {
    final SecureCopyParameters parameters = new SecureCopyParameters();
    parameters.setSourceRoot(smartMonBatchConfig.getSourceRoot());
    final Set<String> sources = Sets.newHashSet();
    sources.add("scripts/injector_install.sh");
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

  private void installInjector(TargetHost targetHost, ShellExecuteEvent shellExecuteEvent) {
    int code = 0;
    try {
      ShellExecute shellExecutor = new ShellExecute(targetHost);
      shellExecutor.setExecuteEvent(shellExecuteEvent);
      String command = String.format("cd %s/scripts && ./injector_install.sh 2>&1",
        smartMonBatchConfig.getWorkFolder());
      code = shellExecutor.run(command);
    } catch (Exception err) {
      throw new SmartMonException(SmartMonErrno.SSH_COMMAND_ERROR, err.getMessage());
    }
    if (!Objects.equals(0, code)) {
      throw new SmartMonException(SmartMonErrno.SSH_COMMAND_ERROR, "install injector error");
    }
  }

  private void installAgentService(String serviceIp, String hostUuid) {
    uploadAgentInstallFiles(serviceIp);
    doAgentInstall(serviceIp, hostUuid);
  }

  private void uploadAgentInstallFiles(String serviceIp) {
    String filepath = "scripts";
    String filename = "agent_install.sh";
    File file = new File(smartMonBatchConfig.getSourceRoot() + filepath, filename);
    TaskContext.currentTaskContext().getCurrentStep().appendLog("upload file: " + filename);
    try {
      agentClientService.uploadFile(serviceIp, filepath, filename, file);
    } catch (Exception err) {
      throw new RuntimeException(err);
    }
  }

  private void doAgentInstall(String serviceIp, String hostUuid) {
    String command = getAgentInstallCommand(serviceIp, hostUuid);
    TaskContext.currentTaskContext().getCurrentStep().appendLog("execute command: " + command);
    String taskGroupId = agentClientService.executeShellCommand(serviceIp, command);
    Supplier<TaskGroupVo> taskResultSupplier = () -> agentClientService.getTaskResult(serviceIp, taskGroupId);
    AtomicReference<String> logsReference = new AtomicReference<>(StringUtils.EMPTY);
    Consumer<TaskGroupVo> logConsumer = taskGroup -> {
      String message = taskGroup.getTasks().get(0).getSteps().get(0).getStepLog();
      if (StringUtils.isNotEmpty(message)) {
        String logs = logsReference.get();
        String logsIncrement = message.substring(logs.length());
        if (StringUtils.isNotEmpty(logsIncrement)) {
          logsIncrement = logsIncrement.substring(0, logsIncrement.length() - 1);
          TaskContext.currentTaskContext().getCurrentStep().appendLog(logsIncrement);
          logsReference.set(message);
        }
      }
    };
    TaskSynchronizer.awaitCompletion(taskResultSupplier, logConsumer);
  }

  private String getAgentInstallCommand(String serviceIp, String hostUuid) {
    String parameters = String.format("-h %s -g %s --project pbdata", hostUuid, systemConfig.getServerAddress());
    if (hostService.isServer(serviceIp)) {
      parameters += " -n server";
    }
    if (Objects.nonNull(systemConfig.getAgentPort())) {
      parameters += String.format(" -f %s", systemConfig.getAgentPort());
    }
    if (Objects.nonNull(systemConfig.getCollectorPort())) {
      parameters += String.format(" -p %s", systemConfig.getCollectorPort());
    }
    if (Objects.nonNull(systemConfig.getServerPort())) {
      parameters += String.format(" -s %s:%s", systemConfig.getServerAddress(), systemConfig.getServerPort());
    }
    return String.format("cd ./scripts && ./agent_install.sh%s 2>&1", " " + parameters);
  }
}
