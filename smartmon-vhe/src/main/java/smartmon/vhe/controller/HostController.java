package smartmon.vhe.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.controller.vo.HostInitParamVo;
import smartmon.vhe.service.StorageHostService;
import smartmon.vhe.service.dto.VheStorageHostDto;
import smartmon.vhe.service.dto.VheStorageHostInitDto;
import smartmon.vhe.service.feign.SmartStorFeignClient;

@RestController
@RequestMapping("${smartmon.api.prefix:/vhe/api/v2}/hosts")
public class HostController {
  @Autowired
  private StorageHostService hostService;
  @Autowired
  private SmartStorFeignClient client;
  @Autowired
  private TaskManagerService taskManagerService;

  @PostMapping("init")
  public SmartMonResponse<TaskGroup> initHost(@RequestBody List<HostInitParamVo> hostsVo) {
    final TaskGroup taskContext = taskManagerService
      .invokeTask("InitHost", () -> hostService.init(VheStorageHostInitDto.convertToList(hostsVo)));
    return new SmartMonResponse<>(taskContext);
  }

  @GetMapping
  public SmartMonResponse<List<VheStorageHostDto>> getAll() {
    return new SmartMonResponse<>(hostService.listAll());
  }
}
