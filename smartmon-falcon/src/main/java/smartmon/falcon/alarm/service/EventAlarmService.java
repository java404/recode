package smartmon.falcon.alarm.service;

import java.util.List;
import java.util.Map;
import smartmon.falcon.alarm.command.EventAlarmFilterCommand;
import smartmon.falcon.alarm.command.EventNoteCreateCommand;
import smartmon.falcon.alarm.model.Alarm;
import smartmon.falcon.alarm.model.AlarmFilterStrategy;
import smartmon.falcon.alarm.model.Event;
import smartmon.falcon.alarm.model.EventNote;


public interface EventAlarmService {
  List<Alarm> getAlarms(EventAlarmFilterCommand eventAlarmFilterCommand, AlarmFilterStrategy alarmFilterStrategy);

  List<Event> getEventsById(String alarmId);

  void deleteAlarm(String alarmId);

  void deleteAlarms(List<String> alarmIds);

  List<EventNote> getEventNodes(String alarmId);

  void createEventNote(EventNoteCreateCommand eventNoteCreateCommand);
}
