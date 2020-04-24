package smartmon.smartstor.interfaces.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.smartstor.app.DiskAppService;
import smartmon.smartstor.app.command.DiskAddCommand;
import smartmon.smartstor.app.command.DiskDelCommand;
import smartmon.smartstor.interfaces.web.controller.vo.DiskAddVo;
import smartmon.smartstor.interfaces.web.controller.vo.DiskDelVo;
import smartmon.smartstor.interfaces.web.representation.DiskRepresentationService;
import smartmon.smartstor.interfaces.web.representation.dto.DiskDto;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.types.TaskContext;

@Api(tags = "disks")
@RequestMapping("api/v2/disks")
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
  public List<DiskDto> getDisks() {
    return diskRepresentationService.getDisks();
  }

  @ApiOperation("Add disk")
  @PostMapping
  public TaskContext addDisk(@RequestBody DiskAddVo vo) {
    DiskAddCommand command = new DiskAddCommand();
    BeanUtils.copyProperties(vo, command);
    return taskManagerService.invokeTask("AddDisk", () -> diskAppService.addDisk(command));
  }

  @ApiOperation("Del disk")
  @DeleteMapping
  public TaskContext delDisk(@RequestBody DiskDelVo vo) {
    DiskDelCommand command = new DiskDelCommand();
    BeanUtils.copyProperties(vo, command);
    return taskManagerService.invokeTask("DelDisk", () -> diskAppService.delDisk(command));
  }
}
