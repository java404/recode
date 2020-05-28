package smartmon.falcon.graph.command;

import java.util.Set;

import lombok.Data;
import smartmon.falcon.graph.model.MergerTypeEnum;

@Data
public class GraphHistoryQueryCommand {
  private Set<String> hosts;
  private Set<String> counters;
  private MergerTypeEnum mergerType;
  private Long startTime;
  private Long endTime;
  private Integer step;
  private Long range;
}
