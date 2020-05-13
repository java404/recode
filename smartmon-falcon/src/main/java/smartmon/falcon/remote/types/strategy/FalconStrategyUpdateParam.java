package smartmon.falcon.remote.types.strategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconStrategyUpdateParam {
  private Long id;
  private String metric;
  private String tags;
  @JsonProperty("max_step")
  private Long maxStep;
  private Integer priority;
  private String func;
  private String op;
  @JsonProperty("right_value")
  private String rightValue;
  private String note;
  @JsonProperty("run_begin")
  private String runBegin;
  @JsonProperty("run_end")
  private String runEnd;
  private Integer pause;
}
