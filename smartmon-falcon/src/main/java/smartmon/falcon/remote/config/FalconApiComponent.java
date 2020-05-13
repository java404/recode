package smartmon.falcon.remote.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.client.FalconClientService;
import smartmon.falcon.remote.types.FalconApiToken;
import smartmon.utilities.misc.JsonConverter;

@Component
public class FalconApiComponent {
  @Autowired
  private FalconClientService falconClientService;
  @Autowired
  private FalconApiConfig falconApiConfig;

  public FalconClient getFalconClient() {
    return falconClientService.getClient(falconApiConfig.getAddress(), falconApiConfig.getRequestPort());
  }

  /**
   * Get Falcon Api Token.
   * @return
   */
  public Map<String, String> getApiToken() {
    final FalconApiToken falconApiToken = new FalconApiToken(falconApiConfig.getName(),
      falconApiConfig.getSession());
    final String tokenJsonString = JsonConverter.writeValueAsStringQuietly(falconApiToken);
    Map<String, String> apiTokenMap = new HashMap<>();
    apiTokenMap.put("ApiToken", tokenJsonString);
    return apiTokenMap;
  }
}
