package smartmon.falcon.alarm.model;

import lombok.Data;

@Data
public class Event {
  private Integer id;
  private String eventCaseId;
  private Integer step;
  private String cond;
  private Integer status;
  private String timestamp;
}
