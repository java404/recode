package smartmon.falcon.remote.request;

import feign.Headers;
import feign.QueryMap;
import feign.RequestLine;
import smartmon.falcon.remote.types.endpoint.FalconEndpoint;
import smartmon.falcon.remote.types.endpoint.FalconEndpointQueryParam;

import java.util.List;

public interface FalconRequestEndpointManager {
  @RequestLine("GET /graph/endpoint")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"default-token-used-in-server-side\"}"})
  List<FalconEndpoint> listEndpoints(@QueryMap FalconEndpointQueryParam queryParam);
}
