package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PbDataPoolCacheConfigParam {
  @JsonProperty("cache_model")
  private String cacheMode;
  @JsonProperty("stop_through")
  private Boolean stopThrough;
}
