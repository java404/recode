package smartmon.vhe.service.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import smartmon.core.hosts.RemoteHostCommand;
import smartmon.core.hosts.SmartMonHost;
import smartmon.core.hosts.SmartMonHostDetailVo;
import smartmon.core.idc.vo.IdcVo;
import smartmon.core.racks.vo.RackAllocationVo;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.service.dto.HostAddToRackDto;
import smartmon.vhe.service.dto.HostRegistrationDto;

@FeignClient(name = "smartmon-core", path = "${smartmon.api.prefix.core:/core/api/v2}")
public interface SmartmonCoreFeignClient {

  @PostMapping(value = "/hosts", consumes = MediaType.APPLICATION_JSON_VALUE)
  SmartMonResponse<SmartMonHost> addHost(@RequestBody HostRegistrationDto dto);

  @PostMapping(value = "/racks/allocate", consumes = MediaType.APPLICATION_JSON_VALUE)
  SmartMonResponse<String> addHostToRack(@RequestBody HostAddToRackDto dto);

  @GetMapping("/racks/allocated")
  SmartMonResponse<List<RackAllocationVo>> getRacks();

  @GetMapping("/idcs")
  SmartMonResponse<List<IdcVo>> getIdcs();

  @PostMapping(value = "/agents")
  SmartMonResponse<TaskGroupVo> installAgent(@RequestParam("hostUuid") String hostUuid);

  @PostMapping(value = "/agents/injectors", consumes = MediaType.APPLICATION_JSON_VALUE)
  SmartMonResponse<TaskGroupVo> installInjector(@RequestBody RemoteHostCommand remoteHostCommand);

  @GetMapping(value = "/tasks/{id}")
  SmartMonResponse<TaskGroupVo> getByTaskId(@PathVariable("id") String taskGroupId);

  @GetMapping("/hosts/{hostUuid}")
  SmartMonResponse<SmartMonHostDetailVo> getHostInfo(@PathVariable("hostUuid") String hostUuid);
}
