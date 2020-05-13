package smartmon.falcon.remote.request;

import feign.HeaderMap;
import feign.Headers;
import feign.QueryMap;
import feign.RequestLine;
import java.util.List;
import java.util.Map;
import smartmon.falcon.remote.types.endpoint.FalconEndpoint;
import smartmon.falcon.remote.types.endpoint.FalconEndpointQueryParam;

@Headers({"Content-Type: application/json"})
public interface FalconRequestEndpointManager {
  @RequestLine("GET /graph/endpoint")
  List<FalconEndpoint> listEndpoints(@QueryMap FalconEndpointQueryParam queryParam,
                                     @HeaderMap Map<String, String> falconApiToken);
}
