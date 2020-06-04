package smartmon.falcon.alarm.model;

import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.util.Strings;
import smartmon.falcon.strategy.model.Strategy;

public class FilterAlarm {
  private final Alarm alarm;
  private final Strategy strategy;

  public FilterAlarm(Alarm alarm, Strategy strategy) {
    this.alarm = alarm;
    this.strategy = strategy;
  }

  /**
   * get group values.
   */
  public List<String> getGroupValues() {
    AlarmTypeEnum alarmTypeEnum = strategy == null ? null : strategy.getStrategyClass();
    String strategyClass = alarmTypeEnum == null ? Strings.EMPTY : alarmTypeEnum.name();
    String endpoint = alarm.getEndpoint();
    if (alarmTypeEnum == AlarmTypeEnum.STORAGE || alarmTypeEnum == AlarmTypeEnum.STORAGE_CLUSTER) {
      return Arrays.asList(strategyClass, endpoint, alarm.getId());
    } else {
      return Arrays.asList(strategyClass, endpoint);
    }
  }

  public Integer getSortedValue() {
    return strategy == null ? 0 : strategy.getStrategyLevel();
  }

  public Alarm getAlarm() {
    return alarm;
  }

  public Integer getLevel() {
    return strategy == null || strategy.getStrategyLevel() == null ? 0 : strategy.getStrategyLevel();
  }

}
