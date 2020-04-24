package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

@Data
public class PbDataDiskInfosV30 {
  @JsonProperty("disk_infos")
  private List<PbDataDiskInfoV30> diskInfos;
  @JsonProperty("raid_disk_infos")
  private List<PbDataRaidDiskInfoV30> raidDiskInfos;

  /** data structure convert. */
  public static PbDataDiskInfos adjust(PbDataDiskInfosV30 diskInfosV30) {
    if (diskInfosV30 == null) {
      return null;
    }

    final List<PbDataDiskInfo> diskInfos = new ArrayList<>();
    for (final PbDataDiskInfoV30 diskInfoV30 : CollectionUtils.emptyIfNull(diskInfosV30.getDiskInfos())) {
      diskInfos.add(PbDataDiskInfoV30.adjust(diskInfoV30));
    }

    final PbDataDiskInfos result = new PbDataDiskInfos();
    result.setDiskInfos(diskInfos);

    final List<PbDataRaidDiskInfo> raidDiskInfos = new ArrayList<>();
    for (final PbDataRaidDiskInfoV30 raidDiskInfoV30 : CollectionUtils.emptyIfNull(diskInfosV30.getRaidDiskInfos())) {
      raidDiskInfos.add(PbDataRaidDiskInfoV30.adjust(raidDiskInfoV30));
    }
    result.setRaidDiskInfos(raidDiskInfos);
    return result;
  }
}
