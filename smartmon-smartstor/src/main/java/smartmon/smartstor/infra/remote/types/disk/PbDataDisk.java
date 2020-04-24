package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataDisk {
  @JsonProperty("disk_info")
  private PbDataDiskInfo diskInfo;
}
