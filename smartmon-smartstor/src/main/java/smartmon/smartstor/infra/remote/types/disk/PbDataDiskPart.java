package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataDiskPart {
  @JsonProperty("disk_part")
  private Integer diskPart;
  private Long size;
  @JsonProperty("ext_dev_name")
  private String devName;
  @JsonProperty("ext_lht")
  private Long lastHeartbeatTime;
  @JsonProperty("ext_actual_state")
  private Boolean actualState;
}
