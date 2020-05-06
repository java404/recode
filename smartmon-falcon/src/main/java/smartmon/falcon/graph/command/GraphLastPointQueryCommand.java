package smartmon.falcon.graph.command;

import lombok.Data;

@Data
public class GraphLastPointQueryCommand {
  private String endpoint;
  private String counter;
}
