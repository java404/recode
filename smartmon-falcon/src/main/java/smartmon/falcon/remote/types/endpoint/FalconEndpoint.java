package smartmon.falcon.remote.types.endpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FalconEndpoint {
  @JsonProperty("endpoint")
  private String endPoint;
  private Integer id;
}
