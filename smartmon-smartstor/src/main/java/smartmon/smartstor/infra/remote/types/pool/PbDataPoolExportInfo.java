package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataPoolExportInfo {
  @JsonProperty("pool_name")
  private String poolName;
  @JsonProperty("state_str")
  private String stateStr;
  private Long valid;
}
