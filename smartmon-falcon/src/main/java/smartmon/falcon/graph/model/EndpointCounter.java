package smartmon.falcon.graph.model;

import lombok.Data;

@Data
public class EndpointCounter {
  private Integer endpointId;
  private String counter;
  private Integer step;
  private String type;
}
