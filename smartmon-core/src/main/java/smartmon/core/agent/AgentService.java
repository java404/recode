package smartmon.core.agent;

import smartmon.taskmanager.types.TaskGroup;
import smartmon.utilities.misc.TargetHost;

public interface AgentService {
  TaskGroup installInjector(String hostUuid);

  TaskGroup installInjector(TargetHost targetHost);
}
