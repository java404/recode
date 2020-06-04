package smartmon.core.racks.model;

import lombok.Getter;
import smartmon.core.racks.Constants;

@Getter
public class RackPosition {
  private final String idcId;
  private final String rackName;
  private final Integer rackIndex;
  private RackPositionStateEnum rackPositionState;
  private String hostUuid;
  private Integer size;

  public static RackPosition create(String idcId) {
    return create(idcId, Rack.createRackName(), Constants.DEFAULT_INDEX);
  }

  public static RackPosition create(String idcId, String rackName, Integer index) {
    return new RackPosition(idcId, rackName, index);
  }

  private RackPosition(String idcId, String rackName, Integer rackIndex) {
    this.idcId = idcId;
    this.rackName = rackName;
    this.rackIndex = rackIndex;
    this.rackPositionState = RackPositionStateEnum.EMPTY;
  }

  void setAllocation(String hostUuid, Integer size) {
    this.rackPositionState = RackPositionStateEnum.LOCATED;
    this.hostUuid = hostUuid;
    this.size = size;
  }
}
