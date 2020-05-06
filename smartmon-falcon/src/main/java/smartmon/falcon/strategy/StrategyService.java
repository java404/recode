package smartmon.falcon.strategy;

import java.util.List;

import smartmon.falcon.strategy.model.PauseEnum;
import smartmon.falcon.strategy.model.Strategy;

public interface StrategyService {
  List<Strategy> getStrategiesByTemplateId(Integer templateId);

  void setStrategyStatus(Integer strategyId, PauseEnum pause);

  void setStrategyThreshold(Integer strategyId, Integer threshold);
}
