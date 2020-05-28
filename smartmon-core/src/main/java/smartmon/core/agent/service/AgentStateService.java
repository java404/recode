package smartmon.core.agent.service;

import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.agent.AgentStateEnum;
import smartmon.core.hosts.mapper.SmartMonHostMapper;
import smartmon.core.hosts.types.SmartMonHost;
import smartmon.taskmanager.dto.TaskGroupDto;
import smartmon.taskmanager.mapper.TaskManagerMapper;
import smartmon.taskmanager.record.TaskStatus;

@Slf4j
@Service
public class AgentStateService {
  @Autowired
  private SmartMonHostMapper smartMonHostMapper;
  @Autowired
  private TaskManagerMapper taskManagerMapper;

  public void setAgentInstallingState(SmartMonHost smartMonHost, String taskGroupId) {
    smartMonHost.setAgentState(AgentStateEnum.INSTALLING);
    smartMonHost.setAgentTaskId(taskGroupId);
    smartMonHostMapper.updateByPrimaryKey(smartMonHost);
  }

  public void setAgentUninstallingState(SmartMonHost smartMonHost, String taskGroupId) {
    smartMonHost.setAgentState(AgentStateEnum.UNINSTALLING);
    smartMonHost.setAgentTaskId(taskGroupId);
    smartMonHostMapper.updateByPrimaryKey(smartMonHost);
  }

  public void syncAgentState() {
    List<SmartMonHost> smartMonHosts = smartMonHostMapper.selectAll();
    for (SmartMonHost smartMonHost : smartMonHosts) {
      String taskGroupId = smartMonHost.getAgentTaskId();
      if (StringUtils.isEmpty(taskGroupId)) {
        continue;
      }
      AgentStateEnum agentState = smartMonHost.getAgentState();
      if (agentState != AgentStateEnum.INSTALLING && agentState != AgentStateEnum.UNINSTALLING) {
        continue;
      }
      try {
        Long taskGroupIdValue = Long.valueOf(taskGroupId);
        TaskGroupDto taskGroup = taskManagerMapper.findTaskGroupInfo(taskGroupIdValue);
        if (taskGroup == null) {
          log.error("Task group ID[{}] not exists", taskGroupId);
          continue;
        }
        if (Objects.equals(TaskStatus.COMPLETED, taskGroup.getStatus())) {
          AgentStateEnum agentStateNew = AgentStateEnum.transform(agentState, taskGroup.isSuccess());
          smartMonHost.setAgentState(agentStateNew);
          smartMonHostMapper.updateByPrimaryKey(smartMonHost);
        }
      } catch (NumberFormatException formatException) {
        log.error("Task group ID[{}] format error", taskGroupId);
      } catch (Exception err) {
        log.error(String.format("Task group ID[%s], error occurred", taskGroupId), err);
      }
    }
  }
}
