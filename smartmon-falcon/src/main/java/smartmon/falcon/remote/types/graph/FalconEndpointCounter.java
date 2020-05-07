package smartmon.falcon.remote.types.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FalconEndpointCounter {
  private String counter;
  @JsonProperty("endpoint_id")
  private Integer endpointId;
  private Integer step;
  private String type;
}
