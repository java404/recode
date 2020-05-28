package smartmon.falcon.remote.types.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import smartmon.falcon.graph.model.GraphRecord;
import smartmon.utilities.misc.BeanConverter;

import java.util.List;

@Data
public class FalconGraphRecord {
  private String endpoint;
  private String counter;
  @JsonProperty("dstype")
  private String type;
  private Integer step;
  @JsonProperty("Values")
  private List<FalconGraphValue> values;

  public List<GraphRecord.GraphValue> getValues() {
    return BeanConverter.copy(this.values, GraphRecord.GraphValue.class);
  }

  @Data
  public static class FalconGraphValue {
    private Long timestamp;
    private Double value;
  }
}
