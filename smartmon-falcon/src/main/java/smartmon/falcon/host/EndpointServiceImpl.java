package smartmon.falcon.host;

import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.config.FalconApiComponent;
import smartmon.falcon.remote.types.endpoint.FalconEndpoint;
import smartmon.falcon.remote.types.endpoint.FalconEndpointQueryParam;
import smartmon.utilities.misc.BeanConverter;

@Service
public class EndpointServiceImpl implements EndpointService {
  @Autowired
  private FalconApiComponent falconApiComponent;

  @Override
  public List<Endpoint> getEndpoints() {
    return getEndpoints(null);
  }

  @Override
  public List<Endpoint> getEndpoints(String endpointRegex) {
    if (StringUtils.isEmpty(endpointRegex)) {
      endpointRegex = ".";
    }
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconEndpointQueryParam queryParam = new FalconEndpointQueryParam(endpointRegex);
    final List<FalconEndpoint> endpoints = falconClient.getEndpoints(queryParam, falconApiComponent.getApiToken());
    return BeanConverter.copy(endpoints, Endpoint.class);
  }

  public Integer getEndpointId(String endpointRegex) {
    final List<Endpoint> endpoints = this.getEndpoints(endpointRegex);
    final Optional<Endpoint> optional = endpoints.stream()
      .filter(endpoint -> endpointRegex.equals(endpoint.getEndpoint()))
      .findFirst();
    Integer endpointId = null;
    if (optional.isPresent()) {
      endpointId = optional.get().getId();
    }
    return endpointId;
  }
}
