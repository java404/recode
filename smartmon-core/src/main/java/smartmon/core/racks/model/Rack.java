package smartmon.core.racks.model;

import com.google.common.collect.Lists;

import java.util.List;

import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import smartmon.core.racks.Constants;

public class Rack {
  @Getter
  private final String idcId;
  @Getter
  private final String rackName;
  private final List<RackAllocation> rackAllocationList;
  private RackPosition[] rackPositions;

  static String createRackName() {
    return RandomStringUtils.random(Constants.DEFAULT_RACK_NAME_LENGTH, true, true).toLowerCase();
  }

  public static Rack create(String idcId, String rackName, List<RackAllocation> rackAllocationList) {
    Rack rack = new Rack(idcId, rackName, rackAllocationList);
    rack.initRackPosition();
    return rack;
  }

  private Rack(String idcId, String rackName, List<RackAllocation> rackAllocationList) {
    this.idcId = idcId;
    this.rackName = rackName;
    this.rackAllocationList = rackAllocationList != null ? rackAllocationList : Lists.newArrayList();
  }

  private void initRackPosition() {
    this.rackPositions = new RackPosition[Constants.MAX_INDEX];
    for (int i = 0; i < this.rackPositions.length; i++) {
      this.rackPositions[i] = RackPosition.create(this.getIdcId(), this.getRackName(), i);
    }
    for (RackAllocation rackAllocation : this.rackAllocationList) {
      Integer rackIndex = rackAllocation.getRackIndex();
      Integer size = rackAllocation.getSize();
      if (rackIndex != null) {
        size = size == null ? Constants.DEFAULT_SIZE : size;
        for (int i = rackIndex; i <= rackIndex + size - 1; i++) {
          if (i >= 1 && i <= Constants.MAX_INDEX) {
            this.rackPositions[i - 1].setAllocation(rackAllocation.getHostUuid(), size);
          }
        }
      }
    }
  }

  public Integer getAvailablePosition(Integer size) {
    for (int i = 1; i <= this.rackPositions.length; i++) {
      boolean available = positionAvailable(i, size);
      if (available) {
        return i;
      }
    }
    return null;
  }

  public boolean positionAvailable(Integer rackIndex, Integer size) {
    for (int i = rackIndex; i < rackIndex + size; i++) {
      if (i < 1 || i > this.rackPositions.length
        || this.rackPositions[i - 1].getRackPositionState().isNotEmpty()) {
        return false;
      }
    }
    return true;
  }
}
