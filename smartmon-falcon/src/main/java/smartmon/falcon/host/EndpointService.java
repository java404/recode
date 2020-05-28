package smartmon.falcon.host;

import java.util.List;

public interface EndpointService {
  List<Endpoint> getEndpoints();

  List<Endpoint> getEndpoints(String endpointRegex);

  Integer getEndpointId(String endpointRegex);
}
