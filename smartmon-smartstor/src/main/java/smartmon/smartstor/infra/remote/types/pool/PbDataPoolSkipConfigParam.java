package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataPoolSkipConfigParam {
  @JsonProperty("skip_thresh")
  private Integer skipThresh;
}
