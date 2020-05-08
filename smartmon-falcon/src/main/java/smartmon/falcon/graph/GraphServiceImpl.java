package smartmon.falcon.graph;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.falcon.graph.command.GraphHistoryQueryCommand;
import smartmon.falcon.graph.command.GraphLastPointQueryCommand;
import smartmon.falcon.graph.model.EndpointCounter;
import smartmon.falcon.graph.model.GraphRecord;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.client.FalconClientService;
import smartmon.falcon.remote.config.FalconApiConfig;
import smartmon.falcon.remote.types.graph.FalconGraphHistoryQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphRecord;
import smartmon.utilities.misc.BeanConverter;

@Service
public class GraphServiceImpl implements GraphService {
  @Autowired
  private FalconClientService falconClientService;
  @Autowired
  private FalconApiConfig falconApiConfig;

  private FalconClient getFalconClient() {
    return falconClientService.getClient(falconApiConfig.getAddress(), falconApiConfig.getRequestPort());
  }

  @Override
  public List<EndpointCounter> getEndpointCounters(Integer endpointId) {
    // TODO: call GET method api/v1/graph/endpoint_counter?eid={endpointId}
    return Lists.newArrayList();
  }

  @Override
  public List<GraphRecord> getGraphHistory(GraphHistoryQueryCommand queryCommand) {
    // TODO: call POST method api/v1/graph/history
    final FalconClient falconClient = getFalconClient();
    final FalconGraphHistoryQueryParam copy = BeanConverter.copy(queryCommand, FalconGraphHistoryQueryParam.class);
    final List<FalconGraphRecord> graphHistory = falconClient.getGraphHistory(copy);
    return ListUtils.emptyIfNull(BeanConverter.copy(graphHistory, GraphRecord.class));
  }

  public Map<String, Object> monitorQuery(List<GraphRecord> graphRecords) {
    return null;
  }

  @Override
  public List<GraphRecord> getGraphLastPoint(List<GraphLastPointQueryCommand> queryCommands) {
    // TODO: call POST method api/v1/graph/lastpoint
    return Lists.newArrayList();
  }
}
