package smartmon.falcon.remote.request;

import feign.Headers;
import feign.QueryMap;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.falcon.remote.types.graph.FalconEndpointCounter;
import smartmon.falcon.remote.types.graph.FalconEndpointCounterQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphHistoryQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphLastPointQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphRecord;

import java.util.List;

public interface FalconRequestGraphManager {
  @RequestLine("GET /graph/endpoint_counter")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"default-token-used-in-server-side\"}"})
  List<FalconEndpointCounter> getEndpointCounters(@QueryMap FalconEndpointCounterQueryParam queryParam);

  @RequestLine("POST /graph/history")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"default-token-used-in-server-side\"}"})
  List<FalconGraphRecord> getGraphHistory(@RequestBody FalconGraphHistoryQueryParam queryParam);

  @RequestLine("GET /graph/lastpoint")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"default-token-used-in-server-side\"}"})
  List<FalconGraphRecord> getGraphLastPoint(@RequestBody FalconGraphLastPointQueryParam queryParam);
}
