package smartmon.falcon.remote.request;

import feign.Headers;
import feign.QueryMap;
import feign.RequestLine;
import smartmon.falcon.remote.types.strategy.FalconStrategy;
import smartmon.falcon.remote.types.strategy.FalconStrategyQueryParam;

import java.util.List;

public interface FalconRequestStrategyManager {
  @RequestLine("GET /strategy")
  @Headers({"Content-Type: application/json"})
  List<FalconStrategy> getStrategiesByTemplateId(@QueryMap FalconStrategyQueryParam queryParam);


}
