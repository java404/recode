package smartmon.falcon.alarm.service;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import smartmon.falcon.alarm.model.Alarm;
import smartmon.falcon.alarm.model.AlarmEvent;
import smartmon.falcon.alarm.model.AlarmEventStatusEnum;
import smartmon.falcon.strategy.model.Strategy;

@Service
public class AlarmFormatServiceImpl implements AlarmFormatService {
  @Override
  public List<Alarm> format(List<Alarm> alarms) {
    return alarms.stream().map(alarm -> format(alarm, alarm.getStrategy())).collect(Collectors.toList());
  }

  public Alarm format(Alarm alarm, Strategy strategy) {
    addRequiredFields(alarm, strategy);
    formatContentInfo(alarm, strategy);
    return alarm;
  }

  private void addRequiredFields(Alarm alarm, Strategy strategy) {
    addTimeInfo(alarm);
    addOtherFields(alarm, strategy);
  }

  private void addTimeInfo(Alarm alarm) {
    final String events = alarm.getEvents();
    if (StringUtils.isEmpty(events)) {
      return;
    }
    String[] eventArray = events.split(";");
    for (String event : eventArray) {
      AlarmEvent alarmEvent = AlarmEvent.fromAlarmEventText(event);
      if (alarmEvent == null) {
        continue;
      }
      if (alarmEvent.getStatus() == AlarmEventStatusEnum.PROBLEM) {
        alarm.setAlarmTime(alarmEvent.getTimestamp());
        break;
      } else if (alarmEvent.getStatus() == AlarmEventStatusEnum.RECOVER) {
        alarm.setAlarmRecoverTime(alarmEvent.getTimestamp());
      }
    }
  }

  private void addOtherFields(Alarm alarm, Strategy strategy) {
    if (strategy != null) {
      alarm.setAlarmTypeEnum(strategy.getStrategyClass());
    }
  }

  private void formatContentInfo(Alarm alarm, Strategy strategy) {
    if (strategy == null || strategy.getStrategyOptions() == null) {
      return;
    }
    final String resourceTemplate = strategy.getStrategyOptions().getResourceTemplate();
    final String messageTemplate = strategy.getStrategyOptions().getMessageTemplate();
    Map<String, String> tags = parseTags(alarm);
    String resource = formatTemplate(resourceTemplate, tags);
    String message = formatTemplate(messageTemplate, tags);
    alarm.setAlarmObject(resource);
    alarm.setAlarmMessage(message);
  }

  private Map<String, String> parseTags(Alarm alarm) {
    Map<String, String> tags = Maps.newHashMap();
    tags.put("endpoint", alarm.getEndpoint());
    tags.put("hostname", alarm.getEndpoint());
    if (StringUtils.isNotEmpty(alarm.getIp())) {
      tags.put("ip", alarm.getIp());
      tags.put("endpoint", String.format("%s(%s)", alarm.getIp(), alarm.getEndpoint()));
    }
    tags.put("threshold", alarm.getThreshold());
    tags.put("op", alarm.getOp());
    tags.put("value", alarm.getValue());
    String metric = alarm.getMetric();
    final int slantingBarIndex = metric.indexOf("/");
    if (slantingBarIndex > 0) {
      final String tagsText = metric.substring(slantingBarIndex + 1);
      for (String tag : tagsText.split(",")) {
        final String[] tagItems = tag.split("=");
        if (tagItems.length != 2) {
          continue;
        }
        String tagName = tagItems[0];
        String tagValue = tagItems[1];
        tags.put(tagName, tagValue);
      }
    }
    return tags;
  }

  private String formatTemplate(String template, Map<String, String> tags) {
    String result = template;
    for (Map.Entry<String, String> entry : tags.entrySet()) {
      result = result.replace(String.format("{%s}", entry.getKey()), entry.getValue());
    }
    result = handleOptionalChunks(result);
    return result;
  }

  /* template contains optional chunks closed by symbols [ and ],
   * if placeholders closed by symbols { and } in chunk is not replaced by value,
   * the chunk will be removed, otherwise the closed symbols of the chunk will be removed
   *
   * template example: 节点{endpoint},磁盘{device}[,槽位{ctl}:{eid}:{slot}]
   */
  private String handleOptionalChunks(String template) {
    Pattern pattern = Pattern.compile("\\[[^\\[\\]]*\\{[^\\[\\]{}]*}[^\\[\\]]*]");
    Matcher matcher = pattern.matcher(template);
    template = matcher.replaceAll("").replaceAll("[\\[\\]]", "");
    if (template.startsWith(",") || template.startsWith("，")) {
      template = template.substring(1);
    }
    if (template.endsWith(",") || template.endsWith("，")) {
      template = template.substring(0, template.length() - 1);
    }
    return template.trim();
  }
}
