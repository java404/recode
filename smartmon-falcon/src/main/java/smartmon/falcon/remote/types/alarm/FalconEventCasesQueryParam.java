package smartmon.falcon.remote.types.alarm;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconEventCasesQueryParam {
  private Long endTime;
  private Integer limit;
  @JsonProperty("process_status")
  private String processStatus;
  private Long startTime;
  private String status;
  private List<String> endpoints;
  @JsonProperty("strategy_id")
  private Integer strategyId;
  @JsonProperty("template_id")
  private Integer templateId;
  private String priority;
}
