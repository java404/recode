package smartmon.smartstor.web.dto;

import lombok.Data;

@Data
public class DiskHealthDto {
  private String endpoint;
  private String counter;
  private GraphValue value;
  private Boolean isMatch;

  public Boolean isHealth() {
    if (isMatch == null) {
      return null;
    }
    return !isMatch;
  }

  @Data
  public static class GraphValue {
    private Long timestamp;
    private Double value;
  }
}
