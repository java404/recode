package smartmon.core.agent;

public enum AgentStateEnum {
  INSTALLING, INSTALL_SUCCESS, INSTALL_FAILED, UNINSTALLING, UNINSTALL_SUCCESS, UNINSTALL_FAILED;

  public static AgentStateEnum transform(AgentStateEnum agentState, boolean success) {
    if (agentState == AgentStateEnum.INSTALLING) {
      return success ? AgentStateEnum.INSTALL_SUCCESS : AgentStateEnum.INSTALL_FAILED;
    } else if (agentState == AgentStateEnum.UNINSTALLING) {
      return success ? AgentStateEnum.UNINSTALL_SUCCESS : AgentStateEnum.UNINSTALL_FAILED;
    }
    return agentState;
  }
}
