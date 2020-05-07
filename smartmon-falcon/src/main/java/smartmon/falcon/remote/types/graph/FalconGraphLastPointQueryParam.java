package smartmon.falcon.remote.types.graph;

import lombok.Data;

@Data
public class FalconGraphLastPointQueryParam {
  private String endpoint;
  private String counter;
}
