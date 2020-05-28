package smartmon.falcon.web.result;

import lombok.Data;

@Data
public class LastGraphValueResult {
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
