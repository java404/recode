package smartmon.falcon.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.falcon.graph.command.GeneralMonitorGraphQueryCommand;
import smartmon.falcon.graph.command.GraphHistoryQueryCommand;
import smartmon.falcon.graph.command.GraphLastPointQueryCommand;
import smartmon.falcon.graph.command.LastGraphValueCompareQueryCommand;
import smartmon.falcon.graph.model.EndpointCounter;
import smartmon.falcon.graph.model.GraphLastRecord;
import smartmon.falcon.graph.model.GraphRecord;
import smartmon.falcon.graph.model.LastGraphValueCompareResult;
import smartmon.falcon.host.EndpointService;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.config.FalconApiComponent;
import smartmon.falcon.remote.types.graph.FalconEndpointCounter;
import smartmon.falcon.remote.types.graph.FalconEndpointCounterQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphHistoryQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphLastPointQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphLastRecord;
import smartmon.falcon.remote.types.graph.FalconGraphRecord;
import smartmon.falcon.strategy.StrategyService;
import smartmon.falcon.strategy.model.Strategy;
import smartmon.falcon.web.result.LastGraphValueResult;
import smartmon.utilities.misc.BeanConverter;

@Slf4j
@Service
public class GraphServiceImpl implements GraphService {
  @Autowired
  private FalconApiComponent falconApiComponent;
  @Autowired
  private EndpointService endpointService;
  @Autowired
  private StrategyService strategyService;

  @Override
  public List<EndpointCounter> getEndpointCounters(Integer endpointId) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconEndpointCounterQueryParam queryParam = new FalconEndpointCounterQueryParam();
    queryParam.setEid(String.valueOf(endpointId));
    final List<FalconEndpointCounter> endpointCounters = falconClient.getEndpointCounters(queryParam,
      falconApiComponent.getApiToken());
    return BeanConverter.copy(endpointCounters, EndpointCounter.class);
  }

  @Override
  public List<EndpointCounter> getEndpointCounters(List<Integer> endpointIds,
                                                   Integer limit, Integer page, String metricPrefix) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconEndpointCounterQueryParam queryParam = new FalconEndpointCounterQueryParam();
    String eids = null;
    if (CollectionUtils.isNotEmpty(endpointIds)) {
      eids = endpointIds.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
    queryParam.setEid(eids);
    queryParam.setLimit(limit);
    queryParam.setPage(page);
    queryParam.setMetricQuery(metricPrefix);
    final List<FalconEndpointCounter> endpointCounters = falconClient.getEndpointCounters(queryParam,
      falconApiComponent.getApiToken());
    return BeanConverter.copy(endpointCounters, EndpointCounter.class);
  }

  @Override
  public List<GraphRecord> getGraphHistory(GraphHistoryQueryCommand queryCommand) {
    final List<GraphRecord> graphRecords = new ArrayList<>();
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconGraphHistoryQueryParam queryParam = new FalconGraphHistoryQueryParam();
    queryParam.setCounters(queryCommand.getCounters());
    queryParam.setHosts(queryCommand.getHosts());
    queryParam.setEndTime(queryCommand.getEndTime());
    queryParam.setStartTime(queryCommand.getStartTime());
    queryParam.setRange(queryCommand.getRange());
    queryParam.setMergerType(queryCommand.getMergerType() != null ? queryCommand.getMergerType().toString() : null);
    final List<FalconGraphRecord> graphHistory = falconClient.getGraphHistory(queryParam.handleParam(),
      falconApiComponent.getApiToken());
    if (CollectionUtils.isNotEmpty(graphHistory)) {
      for (FalconGraphRecord falconGraphRecord : graphHistory) {
        GraphRecord graphRecord = new GraphRecord();
        graphRecord.setGraphValues(BeanConverter.copy(falconGraphRecord.getValues(), GraphRecord.GraphValue.class));
        graphRecord.setType(falconGraphRecord.getType());
        graphRecord.setStep(falconGraphRecord.getStep());
        graphRecord.setEndpoint(falconGraphRecord.getEndpoint());
        graphRecord.setCounter(falconGraphRecord.getCounter());
        graphRecords.add(graphRecord);
      }
    }
    return graphRecords;
  }

  @Override
  public List<GraphLastRecord> getGraphLastPoint(List<GraphLastPointQueryCommand> queryCommands) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final List<FalconGraphLastPointQueryParam> queryParams = BeanConverter.copy(queryCommands,
      FalconGraphLastPointQueryParam.class);
    final List<FalconGraphLastRecord> graphLastPoint = falconClient.getGraphLastPoint(queryParams,
      falconApiComponent.getApiToken());
    return BeanConverter.copy(graphLastPoint, GraphLastRecord.class);

  }

  @Override
  public List<GraphRecord> generalGraphHistory(GeneralMonitorGraphQueryCommand queryCommand) {
    List<GraphRecord> graphRecords = new ArrayList<>();
    Set<String> counterSet = this.getCounterSet(queryCommand);
    try {
      if (CollectionUtils.isNotEmpty(counterSet)) {
        Set<String> hostNames = new HashSet<>();
        hostNames.add(queryCommand.getHostUuid());

        GraphHistoryQueryCommand graphHistoryQueryCommand = new GraphHistoryQueryCommand();
        graphHistoryQueryCommand.setHosts(hostNames);
        graphHistoryQueryCommand.setCounters(counterSet);
        graphHistoryQueryCommand.setEndTime(queryCommand.getEndTime());
        graphHistoryQueryCommand.setStartTime(queryCommand.getStartTime());
        graphHistoryQueryCommand.setMergerType(queryCommand.getMergerType());
        graphHistoryQueryCommand.setRange(queryCommand.getRange());
        graphHistoryQueryCommand.setStep(queryCommand.getStep());
        graphRecords = this.getGraphHistory(graphHistoryQueryCommand);
      }
    } catch (Exception err) {
      log.warn("General Get Graph History Error: ", err);
    }
    return graphRecords;
  }

  /**
   * Get Counter Set.
   */
  public Set<String> getCounterSet(GeneralMonitorGraphQueryCommand queryCommand) {
    final List<String> counters = queryCommand.getCounters();
    Set<String> counterSet = new HashSet<>();
    try {
      final Integer endpointId = endpointService.getEndpointId(queryCommand.getHostUuid());
      if (CollectionUtils.isNotEmpty(counters)) {
        for (String counter : counters) {
          final List<Integer> endpoints = Collections.singletonList(endpointId);
          String metricPrefix = StringUtils.isNotEmpty(queryCommand.getTag())
            ? counter + " " + queryCommand.getTag() : counter;
          final List<EndpointCounter> endpointCounters = this.getEndpointCounters(endpoints, 1000, 0, metricPrefix);
          counterSet.addAll(endpointCounters.stream().map(EndpointCounter::getCounter).collect(Collectors.toList()));
        }
      }
    } catch (Exception err) {
      log.warn("General Get Endpoint Counter Error: ", err);
    }
    return counterSet;
  }

  @Override
  public List<LastGraphValueResult> compareThresholdResult(LastGraphValueCompareQueryCommand queryCommand) {
    List<LastGraphValueResult> lastGraphValueCompareResultList = new ArrayList<>();
    try {
      GeneralMonitorGraphQueryCommand graphQueryCommand = new GeneralMonitorGraphQueryCommand();
      graphQueryCommand.setCounters(Collections.singletonList(queryCommand.getCounter()));
      graphQueryCommand.setTag(queryCommand.getTag());
      graphQueryCommand.setHostUuid(queryCommand.getHostUuid());
      Set<String> counterSet = this.getCounterSet(graphQueryCommand);
      List<GraphLastPointQueryCommand> queryCommands = new ArrayList<>();
      if (CollectionUtils.isNotEmpty(counterSet)) {
        for (String counter : counterSet) {
          GraphLastPointQueryCommand command = new GraphLastPointQueryCommand();
          command.setCounter(counter);
          command.setEndpoint(queryCommand.getHostUuid());
          queryCommands.add(command);
        }
      }
      final List<GraphLastRecord> graphLastPoint = this.getGraphLastPoint(queryCommands);
      if (CollectionUtils.isNotEmpty(graphLastPoint)) {
        final Map<String, Strategy> strategyMap = strategyService.getStrategyMap(queryCommand.getCounter());
        for (GraphLastRecord graphLastRecord : graphLastPoint) {
          LastGraphValueCompareResult lastGraphValueCompareResult = new LastGraphValueCompareResult();
          final String counter = graphLastRecord.getCounter();
          final LastGraphValueCompareResult.GraphValue value = BeanConverter.copy(graphLastRecord.getValue(),
            LastGraphValueCompareResult.GraphValue.class);
          lastGraphValueCompareResult.setCounter(counter);
          lastGraphValueCompareResult.setEndpoint(graphLastRecord.getEndpoint());
          lastGraphValueCompareResult.setValue(value);
          String metric;
          if (counter.contains("/")) {
            metric = counter.substring(0, counter.indexOf("/"));
          } else {
            metric = counter;
          }
          final Strategy strategy = strategyMap.get(metric);
          final boolean isAlarm = strategyService.judgeStrategy(strategy, value.getValue());
          lastGraphValueCompareResult.setIsMatch(isAlarm);
          LastGraphValueResult lastGraphValueResult = BeanConverter.copy(lastGraphValueCompareResult, LastGraphValueResult.class);
          lastGraphValueResult.setValue(BeanConverter.copy(value, LastGraphValueResult.GraphValue.class));
          lastGraphValueCompareResultList.add(lastGraphValueResult);
        }
      }
    } catch (Exception err) {
      log.warn("Get Disk Health Info Error: ", err);
    }
    return lastGraphValueCompareResultList;
  }
}
