package smartmon.falcon.remote.types.alarm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconEventNoteQueryParam {
  @JsonProperty("event_id")
  private String event_id;
}
