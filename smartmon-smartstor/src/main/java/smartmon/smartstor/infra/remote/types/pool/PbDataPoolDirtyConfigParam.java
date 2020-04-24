package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataPoolDirtyConfigParam {
  @JsonProperty("dirty_thresh_lower")
  private Integer lower;
  @JsonProperty("dirty_thresh_upper")
  private Integer upper;
}
