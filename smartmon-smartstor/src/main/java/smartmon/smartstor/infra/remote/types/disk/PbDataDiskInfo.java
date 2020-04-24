package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import smartmon.smartstor.domain.model.enums.DiskTypeEnum;
import smartmon.smartstor.domain.model.enums.IEnum;
import smartmon.utilities.misc.JsonConverter;

@Data
public class PbDataDiskInfo {
  @JsonProperty("disk_id")
  private String diskId;
  @JsonProperty("disk_name")
  private String diskName;
  @JsonProperty("disk_type")
  private int diskType;
  private Long size;
  @JsonProperty("diskparts")
  private List<PbDataDiskPart> diskparts;
  @JsonProperty("header")
  private PbDataDiskHeader headerUuid;
  @JsonProperty("ext_dev_name")
  private String devName;
  @JsonProperty("ext_actual_state")
  private Boolean actualState;
  @JsonProperty("ext_lht")
  private Long lastHeartbeatTime;
  @JsonProperty("ext_raid_info")
  private PbDataRaidDiskInfo raidInfo;
  @JsonProperty("ext_free_size")
  private Long extFreeSize;
  @JsonProperty("ext_diskpart_to_lun_name")
  private List<PbDataSimpleKv> extDiskpartToLunNames;
  @JsonProperty("ext_diskpart_to_pool_name")
  private List<PbDataSimpleKv> extDiskpartToPoolNames;

  @JsonProperty("nvme_diskhealth_info")
  private PbDataNvmeDiskHealthInfo nvmeDiskHealthInfo;

  public DiskTypeEnum getDiskType() {
    return IEnum.getByCode(DiskTypeEnum.class, this.diskType);
  }

  public String getHeaderUuid() {
    return this.headerUuid != null ? this.headerUuid.getUuid() : null;
  }

  public String getDiskParts() {
    return JsonConverter.writeValueAsStringQuietly(this.diskparts);
  }

  public String getRaidInfo() {
    return JsonConverter.writeValueAsStringQuietly(this.raidInfo);
  }

  public String getExtDiskpartToLunName() {
    return JsonConverter.writeValueAsStringQuietly(this.extDiskpartToLunNames);
  }

  public String getExtDiskpartToPoolNames() {
    return JsonConverter.writeValueAsStringQuietly(this.extDiskpartToPoolNames);
  }
}
