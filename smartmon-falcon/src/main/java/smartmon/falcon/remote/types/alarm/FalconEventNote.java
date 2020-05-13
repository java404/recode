package smartmon.falcon.remote.types.alarm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import smartmon.falcon.alarm.model.EventNoteStatusEnum;

@Data
public class FalconEventNote {
  @JsonProperty("case_id")
  private String caseId;
  @JsonProperty("event_caseId")
  private String eventCaseId;
  private String note;
  private String status;
  private String timestamp;
  private String user;

  public EventNoteStatusEnum getStatus() {
    return this.status != null ? EventNoteStatusEnum.getByStatusName(this.status) : null;
  }
}
