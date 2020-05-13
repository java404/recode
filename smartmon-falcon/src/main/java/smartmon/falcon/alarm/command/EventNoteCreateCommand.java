package smartmon.falcon.alarm.command;

import lombok.Data;

@Data
public class EventNoteCreateCommand {
  private String eventId;
  private String note;
  private String status;
}
