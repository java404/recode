package smartmon.falcon.remote.request;

import feign.HeaderMap;
import feign.Headers;
import feign.QueryMap;
import feign.RequestLine;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.falcon.remote.types.graph.FalconEndpointCounter;
import smartmon.falcon.remote.types.graph.FalconEndpointCounterQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphHistoryQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphLastPointQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphLastRecord;
import smartmon.falcon.remote.types.graph.FalconGraphRecord;


@Headers({"Content-Type: application/json"})
public interface FalconRequestGraphManager {
  @RequestLine("GET /graph/endpoint_counter")
  List<FalconEndpointCounter> getEndpointCounters(@QueryMap FalconEndpointCounterQueryParam queryParam,
                                                  @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("POST /graph/history")
  List<FalconGraphRecord> getGraphHistory(@RequestBody FalconGraphHistoryQueryParam queryParam,
                                          @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("POST /graph/lastpoint")
  List<FalconGraphLastRecord> getGraphLastPoint(@RequestBody List<FalconGraphLastPointQueryParam> queryParam,
                                                @HeaderMap Map<String, String> falconApiToken);
}
