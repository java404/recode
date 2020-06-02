package smartmon.vhe.service.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.service.dto.NodeConfDto;

@FeignClient(name = "smartmon-falcon", path = "${smartmon.api.prefix.core:/falcon/api/v2}")
public interface SmartmonFalconFeignClient {

  @GetMapping(value = "/node_configs")
  SmartMonResponse<List<NodeConfDto>> getNodeConfs();
}
