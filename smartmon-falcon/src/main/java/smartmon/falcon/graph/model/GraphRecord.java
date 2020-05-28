package smartmon.falcon.graph.model;

import java.util.List;

import lombok.Data;

@Data
public class GraphRecord {
  private String endpoint;
  private String counter;
  private String type;
  private Integer step;
  private List<GraphValue> graphValues;

  @Data
  public static class GraphValue {
    private Long timestamp;
    private Double value;
  }
}
