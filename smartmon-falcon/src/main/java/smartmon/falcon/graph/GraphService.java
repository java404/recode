package smartmon.falcon.graph;

import java.util.List;

import smartmon.falcon.graph.command.GraphHistoryQueryCommand;
import smartmon.falcon.graph.command.GraphLastPointQueryCommand;
import smartmon.falcon.graph.model.EndpointCounter;
import smartmon.falcon.graph.model.GraphRecord;

public interface GraphService {
  List<EndpointCounter> getEndpointCounters(Integer endpointId);

  List<GraphRecord> getGraphHistory(GraphHistoryQueryCommand queryCommand);

  List<GraphRecord> getGraphLastPoint(List<GraphLastPointQueryCommand> queryCommands);
}
