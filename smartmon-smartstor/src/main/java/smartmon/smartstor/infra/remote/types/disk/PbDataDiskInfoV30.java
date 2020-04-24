package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import lombok.Data;
import smartmon.utilities.misc.BeanConverter;

@Data
public class PbDataDiskInfoV30 {
  @JsonProperty("disk_name")
  private String diskName;
  @JsonProperty("dev_name")
  private String devName;
  @JsonProperty("disk_type")
  private int diskType;
  private Long size;
  @JsonProperty("diskparts")
  private List<PbDataDiskPartV30> diskPartV30s;
  @JsonProperty("header")
  private PbDataDiskHeader headerUuid;
  @JsonProperty("actual_state")
  private Boolean actualState;
  @JsonProperty("last_heartbeat_time")
  private Long lastHeartbeatTime;
  @JsonProperty("raid_disk_info")
  private PbDataRaidDiskInfoV30 raidDiskInfoV30;
  @JsonProperty("nvme_diskhealth_info")
  private PbDataNvmeDiskHealthInfo nvmeDiskHealthInfo;

  /** data structure convert. */
  public static PbDataDiskInfo adjust(PbDataDiskInfoV30 diskInfoV30) {
    if (diskInfoV30 == null) {
      return null;
    }
    final PbDataDiskInfo result = BeanConverter.copy(diskInfoV30, PbDataDiskInfo.class);
    result.setDiskparts(PbDataDiskPartV30.adjust(diskInfoV30.diskPartV30s));
    result.setRaidInfo(PbDataRaidDiskInfoV30.adjust(diskInfoV30.raidDiskInfoV30));
    return result;
  }
}
