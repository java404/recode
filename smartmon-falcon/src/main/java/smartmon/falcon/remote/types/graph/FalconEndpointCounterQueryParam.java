package smartmon.falcon.remote.types.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconEndpointCounterQueryParam {
  @JsonProperty("eid")
  private String eid;
  private String metricQuery;
  private Integer page;
  private Integer limit;
}
