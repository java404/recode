package smartmon.falcon.remote.types.strategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import smartmon.falcon.alarm.model.AlarmTypeEnum;
import smartmon.falcon.strategy.model.PauseEnum;
import smartmon.falcon.strategy.model.Strategy;
import smartmon.falcon.strategy.model.StrategyOptions;
import smartmon.falcon.strategy.model.StrategyPriorityEnum;
import smartmon.utilities.misc.BeanConverter;
import smartmon.utilities.misc.JsonConverter;

import java.util.ArrayList;
import java.util.List;

@Data
public class FalconStrategy {
  private Long id;
  private String metric;
  private String tags;
  @JsonProperty("max_step")
  private Long maxStep;
  private Integer priority;
  private String func;
  private String op;
  @JsonProperty("right_value")
  private String rightValue;
  private String note;
  @JsonProperty("run_begin")
  private String runBegin;
  @JsonProperty("run_end")
  private String runEnd;
  @JsonProperty("tpl_id")
  private Long templateId;
  private PauseEnum pause;
  @JsonProperty("strategy_options")
  private String falconStrategyOptions;
  @JsonProperty("strategy_level")
  private Integer strategyLevel;
  @JsonProperty("strategy_class")
  private String strategyClass;

  public AlarmTypeEnum getStrategyClass() {
    return StringUtils.isNotEmpty(this.strategyClass) ? AlarmTypeEnum.getByName(this.strategyClass) : null;
  }

  public StrategyPriorityEnum getPriority() {
    return this.priority != null ? StrategyPriorityEnum.getByIndex(priority) : null;
  }

  public StrategyOptions getFalconStrategyOptions() {
    return StringUtils.isNotEmpty(falconStrategyOptions) ?
      JsonConverter.readValueQuietly(this.falconStrategyOptions, StrategyOptions.class) : null;
  }

  public static List<Strategy> convertStrategy(List<FalconStrategy> falconStrategies) {
    List<Strategy> strategies = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(falconStrategies)) {
      for (FalconStrategy falconStrategy : falconStrategies) {
        final Strategy strategy = BeanConverter.copy(falconStrategy, Strategy.class);
        strategy.setStrategyOptions(falconStrategy.getFalconStrategyOptions());
        strategies.add(strategy);
      }
    }
    return strategies;
  }
}
