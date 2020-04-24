package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataPoolCacheConfigParam {
  @JsonProperty("cache_model")
  private String cacheMode;
  @JsonProperty("stop_through")
  private Boolean stopThrough;
}
