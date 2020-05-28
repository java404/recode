package smartmon.falcon.remote.request;

import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.strategy.FalconStrategy;
import smartmon.falcon.remote.types.strategy.FalconStrategyQueryParam;
import smartmon.falcon.remote.types.strategy.FalconStrategyUpdateParam;

@Headers({"Content-Type: application/json"})
public interface FalconRequestStrategyManager {
  @RequestLine("GET /strategy")
  @Headers({"Content-Type: application/json"})
  List<FalconStrategy> getStrategiesByTemplateId(@QueryMap FalconStrategyQueryParam queryParam,
                                                 @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("GET /strategy/{strategyId}")
  @Headers({"Content-Type: application/json"})
  FalconStrategy getStrategyById(@Param("strategyId") Integer strategyId,
                                               @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("PUT /strategy")
  @Headers({"Content-Type: application/json"})
  FalconResponseData updateStrategy(@RequestBody FalconStrategyUpdateParam updateParam,
                                    @HeaderMap Map<String, String> falconApiToken);

}
