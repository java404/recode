package smartmon.vhe.service.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.core.racks.vo.IdcRackAllocateVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.service.dto.SmartmonCoreHostAddResDto;
import smartmon.vhe.service.feign.types.SmartMonHostAddParam;

@FeignClient(name = "smartmon-core")
public interface SmartmonCoreFeignClient {

  @PostMapping(value = "/api/v2/hosts/addhosts", consumes = MediaType.APPLICATION_JSON_VALUE)
  SmartMonResponse<List<SmartmonCoreHostAddResDto>> addHosts(@RequestBody  List<SmartMonHostAddParam> hosts);

  @PostMapping(value = "/api/v2/racks/allocate-batch", consumes = MediaType.APPLICATION_JSON_VALUE)
  SmartMonResponse<String> addHostToRackBatch(@RequestBody List<IdcRackAllocateVo> vos);
}
