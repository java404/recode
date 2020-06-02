package smartmon.core.agent;

import smartmon.taskmanager.types.TaskGroup;
import smartmon.utilities.misc.TargetHost;

public interface AgentService {
  TaskGroup installAgent(String hostUuid);

  TaskGroup uninstallAgent(String hostUuid);

  TaskGroup installInjector(TargetHost targetHost);
}
