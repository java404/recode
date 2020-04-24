package smartmon.smartstor.infra.remote.types.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataSmartNvme {
  @JsonProperty("last_port")
  private Integer lastPort;
}
