package smartmon.falcon.alarm.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.hosts.SmartMonHost;
import smartmon.core.provider.SmartMonCoreProvider;
import smartmon.falcon.alarm.command.EventAlarmFilterCommand;
import smartmon.falcon.alarm.command.EventNoteCreateCommand;
import smartmon.falcon.alarm.model.Alarm;
import smartmon.falcon.alarm.model.AlarmFilterStrategy;
import smartmon.falcon.alarm.model.Event;
import smartmon.falcon.alarm.model.EventNote;
import smartmon.falcon.alarm.model.FilterAlarm;
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
import smartmon.falcon.template.TemplateService;
import smartmon.utilities.misc.BeanConverter;

@Service
public class EventAlarmServiceImpl implements EventAlarmService {
  @Autowired
  private FalconApiComponent falconApiComponent;
  @Autowired
  private StrategyService strategyService;
  @Autowired
  private TemplateService templateService;
  @Autowired
  private AlarmFormatService alarmFormatService;

  @DubboReference(version = "${dubbo.service.version}", check = false, lazy = true)
  private SmartMonCoreProvider smartMonCoreProvider;

  private static final String FILTER_VALID_ALARM_STATUS = "PROBLEM";


  @Override
  public List<Alarm> getAlarms(EventAlarmFilterCommand eventAlarmFilterCommand, AlarmFilterStrategy alarmFilterStrategy) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconEventCasesQueryParam queryParam = BeanConverter.copy(eventAlarmFilterCommand,
      FalconEventCasesQueryParam.class);
    final FalconEventCases falconEventCases = falconClient.listEventCases(queryParam, falconApiComponent.getApiToken());
    List<Alarm> alarms = BeanConverter.copy(falconEventCases.getEvents(), Alarm.class);
    if (CollectionUtils.isNotEmpty(alarms)) {
      setAlarmStrategyRelation(alarms);
      setAlarmHostIp(alarms);
      alarms = filterValidAlarms(alarms, alarmFilterStrategy);
      alarms = alarmFormatService.format(alarms);
    }
    return alarms;
  }

  /**
   * set alarm strategy.
   */
  private void setAlarmStrategyRelation(List<Alarm> alarms) {
    final List<Strategy> strategies = strategyService.getStrategies();
    alarms.forEach(alarm -> {
      Strategy strategy = strategyService.getStrategyById(alarm.getStrategyId(), strategies);
      alarm.setStrategy(strategy);
    });
  }

  /**
   * set alarm host ip.
   */
  private void setAlarmHostIp(List<Alarm> alarms) {
    final List<SmartMonHost> smartMonHosts = smartMonCoreProvider.getSmartMonHosts();
    alarms.forEach(alarm -> {
      if (CollectionUtils.isNotEmpty(smartMonHosts)) {
        for (SmartMonHost smartMonHost : smartMonHosts) {
          if (alarm.getEndpoint().equals(smartMonHost.getHostUuid())) {
            alarm.setIp(smartMonHost.getManageIp());
            break;
          }
        }
      }
    });
  }

  /**
   * filter valid alarms.
   */
  public List<Alarm> filterValidAlarms(List<Alarm> alarms, AlarmFilterStrategy alarmFilterStrategy) {
    if (alarmFilterStrategy != AlarmFilterStrategy.LEVEL_PRIORITY) {
      return alarms;
    }
    Map<List<String>, List<FilterAlarm>> filterAlarmMap = alarms.stream()
      .filter(alarm -> FILTER_VALID_ALARM_STATUS.equalsIgnoreCase(alarm.getStatus()))
      .map(alarm -> new FilterAlarm(alarm, alarm.getStrategy()))
      .collect(Collectors.groupingBy(FilterAlarm::getGroupValues, Collectors.toList()));
    List<Alarm> validAlarms = Lists.newArrayList();
    for (List<FilterAlarm> alarmsList : filterAlarmMap.values()) {
      alarmsList.sort(Comparator.comparing(FilterAlarm::getSortedValue));
      Integer maxLevel = alarmsList.get(0).getLevel();
      alarmsList.stream().filter(filterAlarm -> filterAlarm.getLevel().equals(maxLevel))
        .forEach(filterAlarm -> validAlarms.add(filterAlarm.getAlarm()));
    }
    return validAlarms;
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
