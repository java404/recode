package smartmon.falcon.remote.request;

import feign.QueryMap;
import feign.RequestLine;
import smartmon.falcon.remote.types.endpoint.FalconEndpoint;
import smartmon.falcon.remote.types.endpoint.FalconEndpointQueryParam;

import java.util.List;

public interface FalconRequestEndpointManager {
  @RequestLine("GET /graph/endpoint")
  List<FalconEndpoint> listEndpoints(@QueryMap FalconEndpointQueryParam queryParam);
}
