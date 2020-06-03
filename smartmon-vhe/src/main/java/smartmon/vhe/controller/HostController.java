package smartmon.vhe.controller;

import io.swagger.annotations.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.controller.vo.HostInitParamVo;
import smartmon.vhe.service.HostService;
import smartmon.vhe.service.dto.VheStorageHostDto;
import smartmon.vhe.service.feign.SmartStorFeignClient;
import smartmon.webtools.page.SmartMonPageResponseBuilder;

@Api(tags = "hosts")
@RestController
@RequestMapping("${smartmon.api.prefix:/vhe/api/v2}/hosts")
public class HostController {
  @Autowired
  private HostService hostService;
  @Autowired
  private SmartStorFeignClient client;

  @PostMapping("init")
  public SmartMonResponse<TaskGroupVo> initHost(@RequestBody List<HostInitParamVo> hostsVo) {
    TaskGroup taskGroup = hostService.init(HostInitParamVo.toDtos(hostsVo));
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @GetMapping
  public SmartMonResponse<Page<VheStorageHostDto>> getAll(ServerHttpRequest request) {
    return new SmartMonPageResponseBuilder<VheStorageHostDto>(hostService.listAll(),
      request, "listenIp").build();
  }
}
