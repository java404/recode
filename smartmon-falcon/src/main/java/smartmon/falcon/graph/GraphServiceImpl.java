package smartmon.falcon.graph;

import com.google.common.collect.Lists;

import java.util.List;

import org.springframework.stereotype.Service;
import smartmon.falcon.graph.command.GraphHistoryQueryCommand;
import smartmon.falcon.graph.command.GraphLastPointQueryCommand;
import smartmon.falcon.graph.model.EndpointCounter;
import smartmon.falcon.graph.model.GraphRecord;

@Service
public class GraphServiceImpl implements GraphService {
  @Override
  public List<EndpointCounter> getEndpointCounters(Integer endpointId) {
    // TODO: call GET method api/v1/graph/endpoint_counter?eid={endpointId}
    return Lists.newArrayList();
  }

  @Override
  public List<GraphRecord> getGraphHistory(GraphHistoryQueryCommand queryCommand) {
    // TODO: call POST method api/v1/graph/history
    return Lists.newArrayList();
  }

  @Override
  public List<GraphRecord> getGraphLastPoint(List<GraphLastPointQueryCommand> queryCommands) {
    // TODO: call POST method api/v1/graph/lastpoint
    return Lists.newArrayList();
  }
}
