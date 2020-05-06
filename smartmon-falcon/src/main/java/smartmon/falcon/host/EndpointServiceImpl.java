package smartmon.falcon.host;

import com.google.common.collect.Lists;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class EndpointServiceImpl implements EndpointService {
  @Override
  public List<Endpoint> getEndpoints() {
    return getEndpoints(null);
  }

  @Override
  public List<Endpoint> getEndpoints(String endpointRegex) {
    if (StringUtils.isEmpty(endpointRegex)) {
      endpointRegex = ".";
    }
    // TODO: call get method api/v1/graph/endpoint?q={endpointRegex}
    return Lists.newArrayList();
  }
}
