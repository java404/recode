package smartmon.falcon.remote.types.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@Data
public class FalconGraphHistoryQueryParam {
  @JsonProperty("hostnames")
  private Set<String> hosts;
  @JsonProperty("counters")
  private Set<String> counters;
  @JsonProperty("consol_fun")
  private String mergerType;
  @JsonProperty("start_time")
  private Long startTime;
  @JsonProperty("end_time")
  private Long endTime;
  private Integer step;
}
