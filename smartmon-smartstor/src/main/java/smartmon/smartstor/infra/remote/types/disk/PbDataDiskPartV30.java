package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import smartmon.utilities.misc.BeanConverter;

@Data
public class PbDataDiskPartV30 {
  @JsonProperty("disk_part")
  private Integer diskPart;
  private Long size;
  @JsonProperty("dev_name")
  private String devName;
  @JsonProperty("last_heartbeat_time")
  private Long lastHeartbeatTime;
  @JsonProperty("actual_state")
  private Boolean actualState;

  public static List<PbDataDiskPart> adjust(List<PbDataDiskPartV30> pbDataDiskPartV30) {
    return BeanConverter.copy(pbDataDiskPartV30, PbDataDiskPart.class);
  }
}
