package smartmon.falcon.strategy;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import smartmon.falcon.template.Template;
import smartmon.falcon.template.TemplateInfo;
import smartmon.falcon.template.TemplateService;
import smartmon.utilities.misc.BeanConverter;

@Slf4j
@Service
public class StrategyServiceImpl implements StrategyService {
  @Autowired
  private FalconApiComponent falconApiComponent;
  @Autowired
  private TemplateService templateService;

  @Override
  public List<Strategy> getStrategies() {
    List<Strategy> strategies = Lists.newArrayList();
    final List<Template> templates = templateService.listTemplate();
    for (Template template : templates) {
      final TemplateInfo templateInfo = templateService.getTemplateInfoById(template.getId());
      strategies.addAll(templateInfo.getStrategies());
    }
    return strategies;
  }

  @Override
  public List<Strategy> getStrategiesByTemplateId(Integer templateId) {
    return CollectionUtils.emptyIfNull(falconApiComponent.getFalconClient()
      .getStrategiesByTemplateId(FalconStrategyQueryParam.builder()
          .tid(templateId).build(),
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
    if (StringUtils.isNotEmpty(param.getOp())) {
      falconStrategyUpdateParam.setOp(param.getOp());
    }
    if (param.getMaxStep() != null) {
      falconStrategyUpdateParam.setMaxStep(param.getMaxStep());
    }
    if (StringUtils.isNotEmpty(param.getNote())) {
      falconStrategyUpdateParam.setNote(param.getNote());
    }
    if (param.getPriority() != null) {
      falconStrategyUpdateParam.setPriority(param.getPriority());
    }
    if (StringUtils.isNotEmpty(param.getRightValue())) {
      falconStrategyUpdateParam.setRightValue(param.getRightValue());
    }
    return falconClient.updateStrategy(
      falconStrategyUpdateParam,
      token);
  }

  @Override
  public Strategy getStrategyById(Integer strategyId) {
    final FalconStrategy falconStrategy = falconApiComponent.getFalconClient()
      .getStrategyById(strategyId, falconApiComponent.getApiToken());
    final Strategy strategy = BeanConverter.copy(falconStrategy, Strategy.class);
    strategy.setStrategyOptions(falconStrategy.getFalconStrategyOptions());
    return strategy;
  }

  @Override
  public Strategy getStrategyById(Integer strategyId, List<Strategy> strategies) {
    Strategy strategy = null;
    for (Strategy s : strategies) {
      Long id = strategyId.longValue();
      if (id.equals(s.getId())) {
        strategy = s;
        break;
      }
    }
    return strategy;
  }

  @Override
  public Map<String, Strategy> getStrategyMap(String counter) {
    final List<Strategy> strategies = getStrategiesByTemplateId(1);
    Map<String, Strategy> result = new HashMap<>();
    if (CollectionUtils.isNotEmpty(strategies)) {
      for (Strategy strategy : strategies) {
        final String metric = strategy.getMetric();
        if (StringUtils.isNotEmpty(metric) && metric.contains(counter)) {
          result.put(metric, strategy);
        }
      }
    }
    return result;
  }

  @Override
  public boolean judgeStrategy(Strategy strategy, Double value) {
    if (strategy == null || StringUtils.isBlank(strategy.getOp()) || StringUtils.isBlank(strategy.getRightValue())
      || value == null) {
      return false;
    }

    boolean result = false;
    try {
      double criticalValue = Double.parseDouble(strategy.getRightValue());
      final double actualValue = value;
      switch (strategy.getOp()) {
        case "==":
          result = actualValue == criticalValue;
          break;
        case "!=":
          result = actualValue != criticalValue;
          break;
        case ">=":
          result = actualValue >= criticalValue;
          break;
        case "<=":
          result = actualValue <= criticalValue;
          break;
        case "<":
          result = actualValue < criticalValue;
          break;
        case ">":
          result = actualValue > criticalValue;
          break;
        default:
          break;
      }
    } catch (Exception err) {
      log.warn("judge strategy error: ", err);
    }
    return result;
  }
}
