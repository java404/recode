package smartmon.core.agent;

import smartmon.taskmanager.types.TaskGroup;

public interface AgentService {
  TaskGroup installAgent(String hostUuid);

  TaskGroup uninstallAgent(String hostUuid);
}
