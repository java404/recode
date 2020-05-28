package smartmon.smartstor.infra.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.falcon.web.result.LastGraphValueResult;
import smartmon.falcon.web.vo.LastGraphValueCompareQueryVo;
import smartmon.utilities.general.SmartMonResponse;

@FeignClient(name = "smartmon-falcon", path = "${smartmon.api.prefix.falcon:/falcon/api/v2}")
public interface SmartmonFalconFeignClient {

  @PostMapping(value = "/graphs/threshold/compare", consumes = MediaType.APPLICATION_JSON_VALUE)
  SmartMonResponse<List<LastGraphValueResult>> thresholdCompare(@RequestBody LastGraphValueCompareQueryVo vo);

}
