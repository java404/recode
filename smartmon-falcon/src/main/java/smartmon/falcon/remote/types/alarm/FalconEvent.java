package smartmon.falcon.remote.types.alarm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FalconEvent {
  private Integer id;
  @JsonProperty("event_caseId")
  private String eventCaseId;
  private Integer step;
  private String cond;
  private Integer status;
  private String timestamp;
}
