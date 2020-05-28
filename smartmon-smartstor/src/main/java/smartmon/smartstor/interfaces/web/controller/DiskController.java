package smartmon.smartstor.interfaces.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.smartstor.app.DiskAppService;
import smartmon.smartstor.app.command.DiskAddCommand;
import smartmon.smartstor.app.command.DiskDelCommand;
import smartmon.smartstor.app.command.DiskRaidLedCommand;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.interfaces.web.controller.vo.DiskAddVo;
import smartmon.smartstor.interfaces.web.controller.vo.DiskDelVo;
import smartmon.smartstor.interfaces.web.representation.DiskRepresentationService;
import smartmon.smartstor.web.dto.DiskDto;
import smartmon.smartstor.web.dto.StorageDiskDto;
import smartmon.smartstor.web.vo.DiskLedStateVo;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
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
  @GetMapping("{serviceIp}")
  public SmartMonResponse<CachedData<DiskDto>> getDisks(@RequestParam("ServiceIp") String serviceIp) {
    return new SmartMonResponse<>(diskRepresentationService.getDisks(serviceIp, false));
  }

  @ApiOperation("Add disk batch")
  @PostMapping("batch")
  public SmartMonResponse<TaskGroupVo> addDiskBatch(@RequestBody List<DiskAddVo> vos) {
    List<TaskDescription> descriptions = new ArrayList<>();
    for (DiskAddVo vo : vos) {
      DiskAddCommand command = new DiskAddCommand();
      BeanUtils.copyProperties(vo, command);
      final TaskDescription description = new TaskDescriptionBuilder()
        .withAction(TaskAct.ACT_ADD).withResource(TaskRes.RES_DISK).withParameters(command)
        .withStep("ADD", "Add disk", () -> diskAppService.addDisk(command))
        .build();
      descriptions.add(description);
    }
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("AddDiskBatch", descriptions);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Del disk batch")
  @DeleteMapping
  public SmartMonResponse<TaskGroupVo> delDiskBatch(@RequestBody List<DiskDelVo> vos) {
    List<TaskDescription> descriptions = new ArrayList<>();
    for (DiskDelVo vo : vos) {
      DiskDelCommand command = new DiskDelCommand();
      BeanUtils.copyProperties(vo, command);
      final TaskDescription description = new TaskDescriptionBuilder()
        .withAction(TaskAct.ACT_DEL).withResource(TaskRes.RES_DISK).withParameters(command)
        .withStep("DELETE", "Delete disk", () -> diskAppService.delDisk(command))
        .build();
      descriptions.add(description);
    }
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("DeleteDiskBatch", descriptions);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Disk raid led state")
  @PatchMapping("raid-led/{state}")
  public SmartMonResponse<TaskGroupVo> diskRaidLedState(@PathVariable("state") String state,
                                                    @RequestBody DiskLedStateVo vo) {
    final DiskRaidLedCommand command = new DiskRaidLedCommand(vo, state);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_PATCH).withResource(TaskRes.RES_DISK).withParameters(command)
      .withStep("PATCH", command.getCommandStepName(), () -> diskAppService.changeRaidLedState(command))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("ChangeRaidLedState", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Get All disks")
  @GetMapping
  public SmartMonResponse<List<StorageDiskDto>> getAllDisks() {
    List<StorageDiskDto> storageDisks = diskRepresentationService.getDisks();
    return new SmartMonResponse<>(storageDisks);
  }

  @ApiOperation("Get all uninit disks")
  @GetMapping("/uninit")
  public SmartMonResponse<List<StorageDiskDto>> getAllUninitDisks() {
    List<StorageDiskDto> storageDisks = diskRepresentationService.getUninitDisks();
    return new SmartMonResponse<>(storageDisks);
  }

  @ApiOperation("Get all available disks")
  @GetMapping("/init")
  public SmartMonResponse<List<StorageDiskDto>> getAllInitDisks(
    @RequestParam(value = "diskType", required = false) String diskType) {
    List<StorageDiskDto> storageDisks = diskRepresentationService.getInitDisks(diskType);
    return new SmartMonResponse<>(storageDisks);
  }

  @ApiOperation("Get all available disks")
  @GetMapping("/available")
  public SmartMonResponse<List<StorageDiskDto>> getAllAvailableDisks(
    @RequestParam(value = "diskType", required = false) String diskType) {
    List<StorageDiskDto> storageDisks = diskRepresentationService.getAvailableDisks(diskType);
    return new SmartMonResponse<>(storageDisks);
  }

  @ApiOperation("Get available disks via serviceIp")
  @GetMapping("/available/{serviceIp}")
  public SmartMonResponse<List<StorageDiskDto>> getAvailableDisks(
    @PathVariable(value = "serviceIp") String serviceIp,
    @RequestParam(value = "diskType", required = false) String diskType) {
    List<StorageDiskDto> storageDisks = diskRepresentationService.getAvailableDisks(diskType, serviceIp);
    return new SmartMonResponse<>(storageDisks);
  }

  @ApiOperation("Get Disk Info")
  @GetMapping("info")
  public SmartMonResponse<DiskDto> getDiskInfo(@RequestParam("serviceIp") String serviceIp,
                                               @RequestParam(value = "diskName", required = false) String diskName,
                                               @RequestParam(value = "devName", required = false) String devName) {
    return new SmartMonResponse<>(diskRepresentationService.getDiskInfo(serviceIp, diskName, devName));
  }
}
