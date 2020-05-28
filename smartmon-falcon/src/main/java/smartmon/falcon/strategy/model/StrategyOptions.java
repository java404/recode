package smartmon.falcon.strategy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StrategyOptions {
  @JsonProperty("threshold_modify")
  private Boolean thresholdModify;
  @JsonProperty("resource_template")
  private String resourceTemplate;
  @JsonProperty("message_template")
  private String messageTemplate;
  private String unit;
  private Float min;
  private Float max;
}
