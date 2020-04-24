package smartmon.smartstor.infra.remote.types.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataSmartScsi {
  @JsonProperty("last_lun")
  private Integer lastLun;
}
