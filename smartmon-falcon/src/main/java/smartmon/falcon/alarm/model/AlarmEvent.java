package smartmon.falcon.alarm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEvent {
  private AlarmEventStatusEnum status;
  private String timestamp;

  public static AlarmEvent fromAlarmEventText(String eventText) {
    String[] eventItems = eventText.split(",");
    if (eventItems.length < 2) {
      return null;
    }
    AlarmEventStatusEnum status = AlarmEventStatusEnum.fromStatus(eventItems[0]);
    String time = eventItems[1];
    if (status == null || StringUtils.isEmpty(time)) {
      return null;
    }
    return new AlarmEvent(status, time);
  }
}
