package smartmon.falcon.strategy;

import com.google.common.collect.Lists;

import java.util.List;

import org.springframework.stereotype.Service;
import smartmon.falcon.strategy.model.PauseEnum;
import smartmon.falcon.strategy.model.Strategy;

@Service
public class StrategyServiceImpl implements StrategyService {
  @Override
  public List<Strategy> getStrategiesByTemplateId(Integer templateId) {
    // TODO: call GET method api/v1/strategy?tid={templateId}
    return Lists.newArrayList();
  }

  @Override
  public void setStrategyStatus(Integer strategyId, PauseEnum pause) {
    // TODO: call PUT method api/v1/strategy
  }

  @Override
  public void setStrategyThreshold(Integer strategyId, Integer threshold) {
    // TODO: call PUT method api/v1/strategy
  }
}
