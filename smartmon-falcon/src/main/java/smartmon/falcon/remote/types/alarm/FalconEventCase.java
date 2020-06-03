package smartmon.falcon.remote.types.alarm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import smartmon.falcon.strategy.model.StrategyPriorityEnum;

@Data
public class FalconEventCase {
  private String id;
  private String endpoint;
  private String metric;
  private String func;
  private String cond;
  private String note;
  private Integer step;
  @JsonProperty("current_step")
  private Integer currentStep;
  private Integer priority;
  private String status;
  private String timestamp;
  @JsonProperty("updateAt")
  private String updateAt;
  @JsonProperty("closed_at")
  private String closedAt;
  @JsonProperty("closed_note")
  private String closedNote;
  @JsonProperty("user_modified")
  private Integer userModified;
  @JsonProperty("tpl_creator")
  private String tplCreator;
  @JsonProperty("expression_id")
  private Integer expressionId;
  @JsonProperty("strategy_id")
  private Integer strategyId;
  @JsonProperty("template_id")
  private Integer templateId;
  @JsonProperty("process_note")
  private Integer processNote;
  @JsonProperty("process_status")
  private String processStatus;
  private String events;

  public StrategyPriorityEnum getPriority() {
    return this.priority != null ? StrategyPriorityEnum.getByIndex(this.priority) : null;
  }
}
