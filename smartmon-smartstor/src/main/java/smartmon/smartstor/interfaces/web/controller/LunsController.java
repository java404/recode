package smartmon.smartstor.interfaces.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.smartstor.app.LunAppService;
import smartmon.smartstor.app.command.LunActiveStateCommand;
import smartmon.smartstor.app.command.LunAddCommand;
import smartmon.smartstor.app.command.LunDelCommand;
import smartmon.smartstor.app.command.LunStateCommand;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.interfaces.web.representation.LunsRepresentationService;
import smartmon.smartstor.web.dto.LunDto;
import smartmon.smartstor.web.dto.SimpleLunDto;
import smartmon.smartstor.web.dto.StorageLunDto;
import smartmon.smartstor.web.vo.LunActiveStateVo;
import smartmon.smartstor.web.vo.LunAddVo;
import smartmon.smartstor.web.vo.LunDelVo;
import smartmon.smartstor.web.vo.LunStateVo;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.BeanConverter;
import smartmon.webtools.page.SmartMonPageParams;
import smartmon.webtools.page.SmartMonPageResponseBuilder;

@Api(tags = "Luns")
@RequestMapping("${smartmon.api.prefix:/smartstor/api/v2}/luns")
@RestController
public class LunsController {
  @Autowired
  private LunsRepresentationService lunsRepresentationService;
  @Autowired
  private LunAppService lunAppService;
  @Autowired
  private TaskManagerService taskManagerService;

  @ApiOperation("list luns via serviceIp")
  @GetMapping("{serviceIp}/simple-luns")
  @SmartMonPageParams
  public SmartMonResponse<Page<SimpleLunDto>> getSimpleLuns(@PathVariable("serviceIp") String serviceIp,
                                                             ServerHttpRequest request) {
    CachedData<SimpleLunDto> simpleLuns = lunsRepresentationService.getSimpleLuns(serviceIp);
    List<SimpleLunDto> dtos = new ArrayList<>();
    if (simpleLuns != null) {
      dtos.addAll(simpleLuns.getData());
    }
    return new SmartMonPageResponseBuilder<>(dtos, request, "groupName").build();
  }

  @ApiOperation("list not-in-group luns via serviceIp")
  @GetMapping("{serviceIp}/simple-luns/not-in-group")
  public SmartMonResponse<CachedData<SimpleLunDto>> getNotInGroupLuns(@PathVariable("serviceIp") String serviceIp,
                                                                      @RequestParam("groupName") String groupName) {
    CachedData<SimpleLunDto> simpleLuns = lunsRepresentationService.getNotInGroupLuns(serviceIp, groupName);
    return new SmartMonResponse<>(simpleLuns);
  }

  @ApiOperation("list luns")
  @GetMapping()
  public SmartMonResponse<List<StorageLunDto>> getStorageLuns() {
    List<StorageLunDto> storageLunDtos =  lunsRepresentationService.listAllStorageLuns();
    return new SmartMonResponse<>(storageLunDtos);
  }

  @ApiOperation("Add lun batch")
  @PostMapping("")
  public SmartMonResponse<TaskGroupVo> addLunBatch(@RequestBody List<LunAddVo> lunAddVos) {
    List<TaskDescription> tasks = new ArrayList<>();
    for (LunAddVo lunAddVo : lunAddVos) {
      LunAddCommand lunAddCommand = BeanConverter.copy(lunAddVo, LunAddCommand.class);
      final TaskDescription description = new TaskDescriptionBuilder()
        .withAction(TaskAct.ACT_ADD).withResource(TaskRes.RES_LUN).withParameters(lunAddCommand)
        .withStep("ADD", "Add lun", () -> lunAppService.addLun(lunAddCommand))
        .build();
      tasks.add(description);
    }
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("BatchAddLun", tasks);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Lun state batch")
  @PatchMapping("state/batch")
  public SmartMonResponse<TaskGroupVo> offlineBatch(@RequestBody List<LunStateVo> lunStateVos) {
    List<TaskDescription> tasks = new ArrayList<>();
    for (LunStateVo lunStateVo : lunStateVos) {
      LunStateCommand lunStateCommand = BeanConverter.copy(lunStateVo, LunStateCommand.class);
      final TaskDescription description = new TaskDescriptionBuilder()
        .withAction(TaskAct.ACT_PATCH).withResource(TaskRes.RES_LUN).withParameters(lunStateCommand)
        .withStep("PATCH", lunStateCommand.getCommandStepName(), () -> lunAppService.changeLunState(lunStateCommand))
        .build();
      tasks.add(description);
    }
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("BatchChangeLunState", tasks);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Get Lun Info")
  @GetMapping("info")
  public SmartMonResponse<LunDto> getLunInfo(@RequestParam("listenIp") String listenIp,
                                              @RequestParam("lunName") String lunName) {
    return new SmartMonResponse<>(lunsRepresentationService.getLunInfo(listenIp, lunName));
  }

  @ApiOperation("Change Lun active state")
  @PatchMapping("active-state")
  public SmartMonResponse<TaskGroupVo> activeLun(@RequestBody LunActiveStateVo activeStateVo) {
    LunActiveStateCommand command = BeanConverter.copy(activeStateVo, LunActiveStateCommand.class);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_PATCH).withResource(TaskRes.RES_LUN).withParameters(command)
      .withStep("PATCH", command.getCommandStepName(), () -> lunAppService.changeActiveState(command))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("ChangeLunActiveState", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Delete lun batch")
  @DeleteMapping("batch")
  public SmartMonResponse<TaskGroupVo> deleteLunBatch(@RequestBody List<LunDelVo> vos) {
    List<TaskDescription> descriptions = new ArrayList<>();
    for (LunDelVo vo : vos) {
      LunDelCommand command = BeanConverter.copy(vo, LunDelCommand.class);
      final TaskDescription description = new TaskDescriptionBuilder()
        .withAction(TaskAct.ACT_DEL).withResource(TaskRes.RES_LUN).withParameters(command)
        .withStep("DELETE", "Delete lun", () -> lunAppService.deleteLun(command))
        .build();
      descriptions.add(description);
    }
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("DeleteLunBatch", descriptions);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

}
