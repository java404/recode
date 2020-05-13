package smartmon.falcon.remote.types.alarm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FalconEventNoteHandleParam {
  @JsonProperty("event_id")
  private String eventId;
  private String note;
  private String status;
}
