package smartmon.smartstor.interfaces.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.smartstor.app.DiskAppService;
import smartmon.smartstor.app.command.DiskAddCommand;
import smartmon.smartstor.app.command.DiskDelCommand;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.interfaces.web.controller.vo.DiskAddVo;
import smartmon.smartstor.interfaces.web.controller.vo.DiskDelVo;
import smartmon.smartstor.interfaces.web.representation.DiskRepresentationService;
import smartmon.smartstor.interfaces.web.representation.dto.DiskDto;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "disks")
@RequestMapping("${smartmon.api.prefix:/smartstor/api/v2}/disks")
@RestController
public class DiskController {
  @Autowired
  private DiskAppService diskAppService;
  @Autowired
  private DiskRepresentationService diskRepresentationService;
  @Autowired
  private TaskManagerService taskManagerService;

  @ApiOperation("Get disks")
  @GetMapping
  public SmartMonResponse<CachedData<DiskDto>> getDisks(@RequestParam("ServiceIp") String serviceIp) {
    return new SmartMonResponse<>(diskRepresentationService.getDisks(serviceIp));
  }

  @ApiOperation("Add disk")
  @PostMapping
  public SmartMonResponse<TaskGroup> addDisk(@RequestBody DiskAddVo vo) {
    DiskAddCommand command = new DiskAddCommand();
    BeanUtils.copyProperties(vo, command);
    final TaskGroup taskGroup = taskManagerService.invokeTask("AddDisk", () -> diskAppService.addDisk(command));
    return new SmartMonResponse<>(taskGroup);
  }

  @ApiOperation("Del disk")
  @DeleteMapping
  public SmartMonResponse<TaskGroup> delDisk(@RequestBody DiskDelVo vo) {
    final DiskDelCommand command = new DiskDelCommand();
    BeanUtils.copyProperties(vo, command);
    final TaskGroup taskGroup = taskManagerService.invokeTask("DelDisk", () -> diskAppService.delDisk(command));
    return new SmartMonResponse<>(taskGroup);
  }
}
