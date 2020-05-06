package smartmon.falcon.graph.command;

import java.util.Set;

import lombok.Data;

@Data
public class GraphHistoryQueryCommand {
  private Set<String> hosts;
  private Set<String> counters;
  private String mergerType;
  private Long startTime;
  private Long endTime;
  private Integer step;
}
