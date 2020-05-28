package smartmon.smartstor.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.domain.model.enums.RaidTypeEnum;
import smartmon.smartstor.domain.share.Entity;

@Data
@EqualsAndHashCode(callSuper = false)
public class RaidDiskInfo extends Entity {
  private RaidTypeEnum raidType;
  private String ctl;
  private Integer eid;
  private Integer slot;
  private String driveType;
  private String protocol;
  private String pciAddr;
  private String size;
  private String state;
  private String devName;
  private Long lastHeartbeatTime;
  private String sn;
  private String vendor;
  private String modelNum;
  private String model;
  private String health;
  private SsdDiskHealthInfo ssdDiskHealthInfo;
  private HddDiskHealthInfo hddDiskHealthInfo;
}
