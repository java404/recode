package smartmon.smartstor.interfaces.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.smartstor.app.PoolAppService;
import smartmon.smartstor.app.command.PoolAddCommand;
import smartmon.smartstor.app.command.PoolDirtyThresholdCommand;
import smartmon.smartstor.app.command.PoolSizeCommand;
import smartmon.smartstor.app.command.PoolSkipThresholdCommand;
import smartmon.smartstor.app.command.PoolSyncLevelCommand;
import smartmon.smartstor.interfaces.web.representation.PoolsRepresentationService;
import smartmon.smartstor.web.dto.PoolDto;
import smartmon.smartstor.web.dto.StoragePoolDto;
import smartmon.smartstor.web.vo.PoolAddVo;
import smartmon.smartstor.web.vo.PoolDirtyThresholdVo;
import smartmon.smartstor.web.vo.PoolSizeVo;
import smartmon.smartstor.web.vo.PoolSkipThresholdVo;
import smartmon.smartstor.web.vo.PoolSyncLevelVo;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.BeanConverter;


@Api(tags = "Pools")
@RequestMapping("${smartmon.api.prefix:/smartstor/api/v2}/pools")
@RestController
public class PoolsController {
  @Autowired
  private PoolsRepresentationService poolsRepresentationService;
  @Autowired
  private PoolAppService poolAppService;
  @Autowired
  private TaskManagerService taskManagerService;

  @ApiOperation("list Pools")
  @GetMapping
  public SmartMonResponse<List<StoragePoolDto>> getStoragePools() {
    List<StoragePoolDto> storagePoolDtos =  poolsRepresentationService.listAllStoragePools();
    return new SmartMonResponse<>(storagePoolDtos);
  }

  @ApiOperation("Get pool info")
  @GetMapping("info")
  public SmartMonResponse<PoolDto> getPoolInfo(@RequestParam("serviceIp") String serviceIp,
                                               @RequestParam("poolName") String poolName) {
    PoolDto poolDto =  poolsRepresentationService.getPoolInfo(serviceIp, poolName);
    return new SmartMonResponse<>(poolDto);
  }

  @ApiOperation("Add pool batch")
  @PostMapping("batch")
  public SmartMonResponse<TaskGroupVo> addDiskBatch(@RequestBody List<PoolAddVo> vos) {
    List<TaskDescription> descriptions = new ArrayList<>();
    for (PoolAddVo vo : vos) {
      PoolAddCommand command = new PoolAddCommand();
      BeanUtils.copyProperties(vo, command);
      final TaskDescription description = new TaskDescriptionBuilder()
        .withAction(TaskAct.ACT_ADD).withResource(TaskRes.RES_POOL).withParameters(command)
        .withStep("ADD", "Add pool", () -> poolAppService.addPool(command))
        .build();
      descriptions.add(description);
    }
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("AddDiskBatch", descriptions);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("list Pools via serviceIp")
  @GetMapping("{serviceIp}")
  public SmartMonResponse<StoragePoolDto> getStoragePools(@PathVariable("serviceIp") String serviceIp) {
    StoragePoolDto storagePoolDto =  poolsRepresentationService.listStoragePools(serviceIp);
    return new SmartMonResponse<>(storagePoolDto);
  }

  @ApiOperation("config pool size")
  @PatchMapping("conf/size")
  public SmartMonResponse<TaskGroupVo> confSize(@RequestBody PoolSizeVo poolSizeVo) {
    PoolSizeCommand command = BeanConverter.copy(poolSizeVo, PoolSizeCommand.class);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_PATCH).withResource(TaskRes.RES_POOL).withParameters(command)
      .withStep("PATCH", "Config pool size", () -> poolAppService.confPoolSize(command))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("ConfPoolSize", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("config pool dirtythresh")
  @PatchMapping("conf/dirtythresh")
  public SmartMonResponse<TaskGroupVo> dirtythresh(@RequestBody PoolDirtyThresholdVo dirtyThreshVo) {
    PoolDirtyThresholdCommand command = BeanConverter.copy(dirtyThreshVo, PoolDirtyThresholdCommand.class);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_PATCH).withResource(TaskRes.RES_POOL).withParameters(command)
      .withStep("PATCH", "Config pool dirty threshold", () -> poolAppService.confDirtythreshold(command))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("ConfPoolDirtythreshold", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("config pool synclevel")
  @PatchMapping("conf/synclevel")
  public SmartMonResponse<TaskGroupVo> syncLevel(@RequestBody PoolSyncLevelVo syncLevelVo) {
    PoolSyncLevelCommand command = BeanConverter.copy(syncLevelVo, PoolSyncLevelCommand.class);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_PATCH).withResource(TaskRes.RES_POOL).withParameters(command)
      .withStep("PATCH", "Config pool syncLevel", () -> poolAppService.confSyncLevel(command))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("ConfPoolSynclevel", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("config pool skip threshold")
  @PatchMapping("conf/skipThreshold")
  public SmartMonResponse<TaskGroupVo> skipThreshold(@RequestBody PoolSkipThresholdVo vo) {
    PoolSkipThresholdCommand command = BeanConverter.copy(vo, PoolSkipThresholdCommand.class);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_PATCH).withResource(TaskRes.RES_POOL).withParameters(command)
      .withStep("PATCH", "Config pool skip threshold", () -> poolAppService.confSkipThreshold(command))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("ConfPoolSkipThreshold", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

}
