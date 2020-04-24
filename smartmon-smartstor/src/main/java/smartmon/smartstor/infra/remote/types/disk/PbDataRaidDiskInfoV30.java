package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import smartmon.utilities.misc.BeanConverter;

@Data
public class PbDataRaidDiskInfoV30 {
  @JsonProperty("raid_type")
  private Integer raidType;
  private String ctl;
  private Integer eid;
  private Integer slot;
  @JsonProperty("drive_type")
  private String driveType;
  private String protocol;
  @JsonProperty("pci_addr")
  private String pciAddr;
  private String size;
  private String model;
  private String state;
  @JsonProperty("dev_name")
  private String devName;
  @JsonProperty("last_heartbeat_time")
  private Long lastHeartbeatTime;
  @JsonProperty("model_num")
  private String modelNum;
  private String health;

  public static PbDataRaidDiskInfo adjust(PbDataRaidDiskInfoV30 pbDataRaidDiskInfoV30) {
    return BeanConverter.copy(pbDataRaidDiskInfoV30, PbDataRaidDiskInfo.class);
  }
}
