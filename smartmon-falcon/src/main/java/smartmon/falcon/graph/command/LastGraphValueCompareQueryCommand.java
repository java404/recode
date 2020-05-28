package smartmon.falcon.graph.command;

import lombok.Data;

@Data
public class LastGraphValueCompareQueryCommand {
  private String tag;
  private String counter;
  private String hostUuid;
}
