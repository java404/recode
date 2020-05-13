package smartmon.falcon.strategy;

import java.util.List;

import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.strategy.FalconStrategyUpdateParam;
import smartmon.falcon.strategy.model.PauseEnum;
import smartmon.falcon.strategy.model.Strategy;

public interface StrategyService {
  List<Strategy> getStrategiesByTemplateId(Integer templateId);

  FalconResponseData setStrategyStatus(Integer strategyId, PauseEnum pause);

  FalconResponseData updateStrategyById(Integer strategyId, FalconStrategyUpdateParam param);

  Strategy getStrategyById(Integer strategyId);
}
