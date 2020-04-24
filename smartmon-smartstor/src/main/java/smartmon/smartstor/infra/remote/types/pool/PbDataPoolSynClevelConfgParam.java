package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataPoolSynClevelConfgParam {
  @JsonProperty("sync_level")
  private Integer syncLevel;
}
