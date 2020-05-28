package smartmon.falcon.graph.model;

import lombok.Data;

@Data
public class GraphLastRecord {
  private String endpoint;
  private String counter;
  private GraphLastRecord.GraphLastValue value;

  @Data
  public static class GraphLastValue {
    private Long timestamp;
    private Double value;
  }
}
