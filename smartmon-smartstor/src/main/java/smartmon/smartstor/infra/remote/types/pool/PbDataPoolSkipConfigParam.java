package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PbDataPoolSkipConfigParam {
  @JsonProperty("skip_thresh")
  private Integer skipThresh;
}
