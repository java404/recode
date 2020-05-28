package smartmon.falcon.remote.types.graph;

import lombok.Data;
import smartmon.falcon.graph.model.GraphLastRecord;
import smartmon.utilities.misc.BeanConverter;

@Data
public class FalconGraphLastRecord {
  private String endpoint;
  private String counter;
  private FalconGraphLastValue value;

  public GraphLastRecord.GraphLastValue getValue() {
    return BeanConverter.copy(this.value, GraphLastRecord.GraphLastValue.class);
  }

  @Data
  public static class FalconGraphLastValue {
    private Long timestamp;
    private Double value;
  }
}
