package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.domain.model.RaidDiskInfo;
import smartmon.utilities.misc.BeanConverter;
import smartmon.utilities.misc.JsonConverter;

@Data
public class PbDataDiskInfos {
  @JsonProperty("disk_infos")
  private List<PbDataDiskInfo> diskInfos;
  @JsonProperty("raid_disk_infos")
  private List<PbDataRaidDiskInfo> raidDiskInfos;

  /** data structure convert. */
  public List<Disk> getDisks() {
    List<Disk> diskList = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(this.diskInfos)) {
      this.diskInfos.forEach(diskInfo -> {
        final Disk copyDiskInfo = BeanConverter.copy(diskInfo, Disk.class);
        if (diskInfo.getNvmeDiskHealthInfo() != null && copyDiskInfo != null) {
          BeanUtils.copyProperties(diskInfo, copyDiskInfo);
        }
        diskList.add(copyDiskInfo);
      });
    }
    if (CollectionUtils.isNotEmpty(raidDiskInfos)) {
      this.raidDiskInfos.forEach(raidDiskInfo -> {
        final Disk copyRaidDiskInfo = BeanConverter.copy(raidDiskInfo, Disk.class);
        if (copyRaidDiskInfo != null) {
          //copyRaidDiskInfo.setRaidInfo(JsonConverter.writeValueAsStringQuietly(raidDiskInfo));
          copyRaidDiskInfo.setRaidInfo(BeanConverter.copy(raidDiskInfo, RaidDiskInfo.class));
          copyRaidDiskInfo.setDiskName("");
          copyRaidDiskInfo.setDevName(raidDiskInfo.getRaidDevName());
          copyRaidDiskInfo.setSize(raidDiskInfo.getRaidSize());
          copyRaidDiskInfo.setExtFreeSize(raidDiskInfo.getRaidSize());
          diskList.add(copyRaidDiskInfo);
        }
      });
    }
    return diskList;
  }
}
