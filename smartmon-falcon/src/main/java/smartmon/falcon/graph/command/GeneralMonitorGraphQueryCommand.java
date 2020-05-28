package smartmon.falcon.graph.command;

import java.util.List;
import lombok.Data;
import smartmon.falcon.graph.model.MergerTypeEnum;

@Data
public class GeneralMonitorGraphQueryCommand {
  private String tag;
  private List<String> counters;
  private String hostUuid;
  private Long startTime;
  private Long endTime;
  private MergerTypeEnum mergerType;
  private Integer step;
  private Long range;
}
