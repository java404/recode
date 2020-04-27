package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataPoolDirtyThresh {
  @JsonProperty("lower")
  private Long dirtyThreshLower;
  @JsonProperty("upper")
  private Long dirtyThreshUpper;
}
