package smartmon.vhe.service.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.core.hosts.SmartMonHost;
import smartmon.core.idc.vo.IdcVo;
import smartmon.core.racks.vo.IdcRackAllocateVo;
import smartmon.core.racks.vo.RackAllocationVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.service.feign.types.SmartMonHostAddParam;

@FeignClient(name = "smartmon-core", path = "${smartmon.api.prefix.core:/core/api/v2}")
public interface SmartmonCoreFeignClient {

  @PostMapping(value = "/hosts/addhosts", consumes = MediaType.APPLICATION_JSON_VALUE)
  SmartMonResponse<List<SmartMonHost>> addHosts(@RequestBody  List<SmartMonHostAddParam> hosts);

  @PostMapping(value = "/racks/allocate-batch", consumes = MediaType.APPLICATION_JSON_VALUE)
  SmartMonResponse<String> addHostToRackBatch(@RequestBody List<IdcRackAllocateVo> vos);

  @GetMapping("/racks/allocated")
  SmartMonResponse<List<RackAllocationVo>> getRacks();

  @GetMapping("/idcs")
  SmartMonResponse<List<IdcVo>> getIdcs();
}
