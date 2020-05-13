package smartmon.falcon.strategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.config.FalconApiComponent;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.strategy.FalconStrategy;
import smartmon.falcon.remote.types.strategy.FalconStrategyQueryParam;
import smartmon.falcon.remote.types.strategy.FalconStrategyUpdateParam;
import smartmon.falcon.strategy.model.PauseEnum;
import smartmon.falcon.strategy.model.Strategy;
import smartmon.utilities.misc.BeanConverter;

@Service
public class StrategyServiceImpl implements StrategyService {
  @Autowired
  private FalconApiComponent falconApiComponent;

  @Override
  public List<Strategy> getStrategiesByTemplateId(Integer templateId) {
    return CollectionUtils.emptyIfNull(falconApiComponent.getFalconClient()
      .getStrategiesByTemplateId(FalconStrategyQueryParam.builder()
          .templateId(templateId).build(),
        falconApiComponent.getApiToken()))
      .stream()
      .map(s -> BeanConverter.copy(s, Strategy.class))
      .collect(Collectors.toList());
  }

  @Override
  public FalconResponseData setStrategyStatus(Integer strategyId, PauseEnum pause) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final Map<String, String> token = falconApiComponent.getApiToken();
    final FalconStrategy falconStrategy = falconClient.getStrategyById(strategyId, token);
    final FalconStrategyUpdateParam falconStrategyUpdateParam = BeanConverter
        .copy(falconStrategy, FalconStrategyUpdateParam.class);
    falconStrategyUpdateParam.setPause(pause.getCode());
    return falconClient.updateStrategy(
      falconStrategyUpdateParam,
      token);
  }

  @Override
  public FalconResponseData updateStrategyById(Integer strategyId,
                                               FalconStrategyUpdateParam param) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final Map<String, String> token = falconApiComponent.getApiToken();
    final FalconStrategy falconStrategy = falconClient.getStrategyById(strategyId, token);
    final FalconStrategyUpdateParam falconStrategyUpdateParam = BeanConverter
        .copy(falconStrategy, FalconStrategyUpdateParam.class);
    falconStrategyUpdateParam.setOp(param.getOp());
    falconStrategyUpdateParam.setMaxStep(param.getMaxStep());
    falconStrategyUpdateParam.setNote(param.getNote());
    falconStrategyUpdateParam.setPriority(param.getPriority());
    falconStrategyUpdateParam.setRightValue(param.getRightValue());
    return falconClient.updateStrategy(
      falconStrategyUpdateParam,
      token);
  }

  @Override
  public Strategy getStrategyById(Integer strategyId) {
    return BeanConverter
      .copy(falconApiComponent.getFalconClient()
        .getStrategyById(strategyId, falconApiComponent.getApiToken()),
        Strategy.class);
  }
}
