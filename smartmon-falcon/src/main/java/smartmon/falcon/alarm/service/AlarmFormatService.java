package smartmon.falcon.alarm.service;

import java.util.List;
import smartmon.falcon.alarm.model.Alarm;

public interface AlarmFormatService {
  List<Alarm> format(List<Alarm> alarms);
}
