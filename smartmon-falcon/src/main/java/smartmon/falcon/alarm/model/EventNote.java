package smartmon.falcon.alarm.model;

import lombok.Data;

@Data
public class EventNote {
  private String caseId;
  private String eventCaseId;
  private String note;
  private EventNoteStatusEnum status;
  private String timestamp;
  private String user;
}
