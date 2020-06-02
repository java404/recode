package smartmon.falcon.alarm.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.falcon.alarm.command.EventAlarmFilterCommand;
import smartmon.falcon.alarm.command.EventNoteCreateCommand;
import smartmon.falcon.alarm.model.Alarm;
import smartmon.falcon.alarm.model.Event;
import smartmon.falcon.alarm.model.EventNote;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.config.FalconApiComponent;
import smartmon.falcon.remote.types.alarm.FalconEventCaseDeleteParam;
import smartmon.falcon.remote.types.alarm.FalconEventCases;
import smartmon.falcon.remote.types.alarm.FalconEventCasesQueryParam;
import smartmon.falcon.remote.types.alarm.FalconEventNoteHandleParam;
import smartmon.falcon.remote.types.alarm.FalconEventNoteQueryParam;
import smartmon.falcon.remote.types.alarm.FalconEventNotes;
import smartmon.falcon.remote.types.alarm.FalconEvents;
import smartmon.falcon.remote.types.alarm.FalconEventsQueryParam;
import smartmon.falcon.strategy.StrategyService;
import smartmon.falcon.strategy.model.Strategy;
import smartmon.utilities.misc.BeanConverter;

@Service
public class EventAlarmServiceImpl implements EventAlarmService {
  @Autowired
  private FalconApiComponent falconApiComponent;
  @Autowired
  private StrategyService strategyService;

  @Override
  public List<Alarm> getAlarms(EventAlarmFilterCommand eventAlarmFilterCommand) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconEventCasesQueryParam queryParam = BeanConverter.copy(eventAlarmFilterCommand,
      FalconEventCasesQueryParam.class);
    final FalconEventCases falconEventCases = falconClient.listEventCases(queryParam, falconApiComponent.getApiToken());
    final List<Alarm> alarms = BeanConverter.copy(falconEventCases.getEvents(), Alarm.class);
    if (CollectionUtils.isNotEmpty(alarms)) {
      setAlarmStrategyRelation(alarms);
    }
    return alarms;
  }

  private void setAlarmStrategyRelation(List<Alarm> alarms) {
    alarms.forEach(alarm -> {
      Strategy strategy = strategyService.getStrategyById(alarm.getStrategyId());
      alarm.setStrategy(strategy);
    });
  }

  @Override
  public List<Event> getEventsById(String alarmId) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconEventsQueryParam param = new FalconEventsQueryParam(alarmId);
    final FalconEvents falconEvents = falconClient.listEvents(param, falconApiComponent.getApiToken());
    return BeanConverter.copy(falconEvents.getEvents(), Event.class);
  }

  @Override
  public void deleteAlarm(String alarmId) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    falconClient.deleteEventCase(alarmId, falconApiComponent.getApiToken());
  }

  @Override
  public void deleteAlarms(List<String> alarmIds) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconEventCaseDeleteParam eventCaseDeleteParam = new FalconEventCaseDeleteParam();
    eventCaseDeleteParam.setAlarms(alarmIds);
    falconClient.batchDeleteEventCase(eventCaseDeleteParam, falconApiComponent.getApiToken());
  }

  @Override
  public List<EventNote> getEventNodes(String alarmId) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconEventNoteQueryParam falconEventNoteQueryParam = new FalconEventNoteQueryParam(alarmId);
    final FalconEventNotes falconEventNotes = falconClient.listEventNotesById(falconEventNoteQueryParam,
      falconApiComponent.getApiToken());
    return BeanConverter.copy(falconEventNotes.getNotes(), EventNote.class);
  }

  @Override
  public void createEventNote(EventNoteCreateCommand eventNoteCreateCommand) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconEventNoteHandleParam falconEventNoteHandleParam = new FalconEventNoteHandleParam();
    falconEventNoteHandleParam.setEventId(eventNoteCreateCommand.getEventId());
    falconEventNoteHandleParam.setNote(eventNoteCreateCommand.getNote());
    falconEventNoteHandleParam.setStatus(eventNoteCreateCommand.getStatus());
    falconClient.createEventNote(falconEventNoteHandleParam, falconApiComponent.getApiToken());
  }
}
