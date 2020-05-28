package smartmon.falcon.graph.model;

import lombok.Data;

@Data
public class LastGraphValueCompareResult {
  private String endpoint;
  private String counter;
  private GraphValue value;
  private Boolean isMatch;

  @Data
  public static class GraphValue {
    private Long timestamp;
    private Double value;
  }
}
