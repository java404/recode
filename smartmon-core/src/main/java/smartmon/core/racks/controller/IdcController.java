package smartmon.core.racks.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.core.mapper.IdcMapper;
import smartmon.core.racks.RackService;
import smartmon.core.racks.model.Idc;
import smartmon.core.racks.vo.IdcAddVo;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "idcs")
@RequestMapping("${smartmon.api.prefix:/core/api/v2}/idcs")
@RestController
public class IdcController {
  @Autowired
  private IdcMapper idcMapper;
  @Autowired
  private RackService rackService;
  @Autowired
  private TaskManagerService taskManagerService;

  @ApiOperation("Get all idc info")
  @GetMapping
  public SmartMonResponse<List<Idc>> getAll() {
    return new SmartMonResponse<>(idcMapper.findAll());
  }

  @ApiOperation("Add idcs")
  @PostMapping("batch")
  public SmartMonResponse<TaskGroupVo> add(@RequestBody List<IdcAddVo> vos) {
    List<String> idcNames = vos.stream().map(IdcAddVo::getName).collect(Collectors.toList());
    Runnable runnable = () -> TaskContext.currentTaskContext().setDetail(rackService.addIdcs(idcNames));
    TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_ADD).withResource(TaskRes.RES_IDC).withParameters(idcNames)
      .withStep("ADD", "add idcs", runnable)
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("AddIdcs", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Add idc")
  @PostMapping
  public SmartMonResponse<Idc> add(@RequestBody IdcAddVo vo) {
    Idc idc = rackService.addIdcIfAbsent(vo.getName());
    return new SmartMonResponse<>(idc);
  }

  @ApiOperation("Rename")
  @PatchMapping("{name}/rename")
  public SmartMonResponse<String> rename(@PathVariable String name, @RequestParam("newName") String newName) {
    rackService.renameIdc(name, newName);
    return SmartMonResponse.OK;
  }
}
