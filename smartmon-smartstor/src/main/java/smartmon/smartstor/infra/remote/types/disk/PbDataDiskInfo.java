package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import smartmon.smartstor.domain.model.DiskPart;
import smartmon.smartstor.domain.model.RaidDiskInfo;
import smartmon.smartstor.domain.model.enums.DiskTypeEnum;
import smartmon.smartstor.domain.model.enums.IEnum;
import smartmon.utilities.misc.BeanConverter;

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

  public List<DiskPart> getDiskParts() {
    //return JsonConverter.writeValueAsStringQuietly(this.diskparts);
    if (CollectionUtils.isEmpty(diskparts)) {
      return Lists.newArrayList();
    }
    for (PbDataDiskPart diskPart : diskparts) {
      diskPart.setDevName(this.devName);
      diskPart.setLastHeartbeatTime(this.lastHeartbeatTime);
      diskPart.setActualState(this.actualState);
    }
    return BeanConverter.copy(diskparts, DiskPart.class);
  }

  public RaidDiskInfo getRaidInfo() {
    return BeanConverter.copy(raidInfo, RaidDiskInfo.class);
    //return JsonConverter.writeValueAsStringQuietly(this.raidInfo);
  }

  public String getExtDiskpartToLunNames() {
    if (CollectionUtils.isEmpty(this.extDiskpartToLunNames)) {
      return null;
    }
    List<String> names = new ArrayList<>();
    for (PbDataSimpleKv kv : this.extDiskpartToLunNames) {
      names.add(kv.toString());
    }
    return StringUtils.join(names, ",");
  }

  public String getExtDiskpartToPoolNames() {
    if (CollectionUtils.isEmpty(this.extDiskpartToPoolNames)) {
      return null;
    }
    List<String> names = new ArrayList<>();
    for (PbDataSimpleKv kv : this.extDiskpartToPoolNames) {
      names.add(kv.toString());
    }
    return StringUtils.join(names, ",");
  }
}
