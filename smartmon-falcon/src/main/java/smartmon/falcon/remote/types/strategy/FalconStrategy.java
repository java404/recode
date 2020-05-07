package smartmon.falcon.remote.types.strategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FalconStrategy {
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
  @JsonProperty("tpl_id")
  private Long templateId;
  private Integer pause;
}