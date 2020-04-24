package smartmon.core.racks.model;

public enum RackPositionStateEnum {
  EMPTY,
  LOCATED;

  public boolean isNotEmpty() {
    return this != RackPositionStateEnum.EMPTY;
  }
}
