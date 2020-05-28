package smartmon.core.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.agent.service.AgentInstallService;
import smartmon.core.agent.service.AgentUninstallService;
import smartmon.taskmanager.types.TaskGroup;

@Slf4j
@Service
public class AgentServiceImpl implements AgentService {
  @Autowired
  private AgentInstallService agentInstallService;
  @Autowired
  private AgentUninstallService agentUninstallService;

  @Override
  public TaskGroup installAgent(String hostUuid) {
    return agentInstallService.installAgent(hostUuid);
  }

  @Override
  public TaskGroup uninstallAgent(String hostUuid) {
    return agentUninstallService.uninstallAgent(hostUuid);
  }
}
