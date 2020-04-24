package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataDiskV30 {
  @JsonProperty("disk_info")
  private PbDataDiskInfoV30 diskInfo;
}
