package smartmon.falcon.remote.types.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FalconGraphRecord {
  private String endpoint;
  private String counter;
  private String dstype;
  private Integer step;
  @JsonProperty("Values")
  private List<FalconGraphValue> values;

  @Data
  private static class FalconGraphValue {
    private Long timestamp;
    private Double value;
  }
}
