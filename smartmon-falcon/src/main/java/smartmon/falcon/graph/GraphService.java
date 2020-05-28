package smartmon.falcon.graph;

import java.util.List;

import smartmon.falcon.graph.command.GeneralMonitorGraphQueryCommand;
import smartmon.falcon.graph.command.GraphHistoryQueryCommand;
import smartmon.falcon.graph.command.GraphLastPointQueryCommand;
import smartmon.falcon.graph.command.LastGraphValueCompareQueryCommand;
import smartmon.falcon.graph.model.EndpointCounter;
import smartmon.falcon.graph.model.GraphLastRecord;
import smartmon.falcon.graph.model.GraphRecord;
import smartmon.falcon.web.result.LastGraphValueResult;


public interface GraphService {
  List<EndpointCounter> getEndpointCounters(Integer endpointId);

  List<EndpointCounter> getEndpointCounters(List<Integer> endpointId, Integer limit, Integer page, String metricPrefix);

  List<GraphRecord> getGraphHistory(GraphHistoryQueryCommand queryCommand);

  List<GraphLastRecord> getGraphLastPoint(List<GraphLastPointQueryCommand> queryCommands);

  List<GraphRecord> generalGraphHistory(GeneralMonitorGraphQueryCommand queryCommand);

  List<LastGraphValueResult> compareThresholdResult(LastGraphValueCompareQueryCommand queryCommand);
}
