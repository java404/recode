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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.smartstor.app.GroupAppService;
import smartmon.smartstor.app.command.GroupAddCommand;
import smartmon.smartstor.app.command.GroupDeleteCommand;
import smartmon.smartstor.app.command.GroupLunAddCommand;
import smartmon.smartstor.app.command.GroupNodeAddCommand;
import smartmon.smartstor.app.command.GroupNodeDelCommand;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.interfaces.web.representation.GroupsRepresentationService;
import smartmon.smartstor.interfaces.web.representation.dto.GroupDto;
import smartmon.smartstor.web.vo.GroupAddVo;
import smartmon.smartstor.web.vo.GroupDeleteVo;
import smartmon.smartstor.web.vo.GroupLunAddVo;
import smartmon.smartstor.web.vo.GroupNodeAddVo;
import smartmon.smartstor.web.vo.GroupNodeDelVo;
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


@Api(tags = "groups")
@RequestMapping("${smartmon.api.prefix:/smartstor/api/v2}/groups")
@RestController
public class GroupsController {
  @Autowired
  private GroupsRepresentationService groupsRepresentationService;
  @Autowired
  private GroupAppService groupAppService;
  @Autowired
  private TaskManagerService taskManagerService;

  @ApiOperation("Get groups")
  @GetMapping
  @SmartMonPageParams
  public SmartMonResponse<Page<GroupDto>> getGroups(ServerHttpRequest request) {
    CachedData<GroupDto> groups = groupsRepresentationService.getGroups();
    List<GroupDto> groupDtos = new ArrayList<>();
    if (groups != null) {
      groupDtos.addAll(groups.getData());
    }
    return new SmartMonPageResponseBuilder<>(groupDtos, request, "listenIp").build();
  }

  @ApiOperation("Add group")
  @PostMapping
  public SmartMonResponse<TaskGroupVo> addGroup(@RequestBody GroupAddVo groupAddVo) {
    GroupAddCommand command = new GroupAddCommand(groupAddVo.getServiceIp(), groupAddVo.getGroupName());
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_ADD).withResource(TaskRes.RES_GROUP).withParameters(command)
      .withStep("ADD", "Add group", () -> groupAppService.addGroup(command))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("AddGroup", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Delete Group")
  @DeleteMapping
  public SmartMonResponse<TaskGroupVo> deleteGroup(@RequestBody GroupDeleteVo deleteVo) {
    GroupDeleteCommand deleteCommand = new GroupDeleteCommand(deleteVo.getServiceIp(), deleteVo.getGroupName());
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_DEL).withResource(TaskRes.RES_GROUP).withParameters(deleteCommand)
      .withStep("DELETE", "Del group", () -> groupAppService.deleteGroup(deleteCommand))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("DelGroup", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Add Group Node")
  @PostMapping("{groupName}/nodes")
  public SmartMonResponse<TaskGroupVo> addGroupNode(@RequestBody GroupNodeAddVo nodeAddVo,
                                                    @PathVariable("groupName") String groupName) {
    GroupNodeAddCommand nodeAddCommand = BeanConverter.copy(nodeAddVo, GroupNodeAddCommand.class);
    nodeAddCommand.setGroupName(groupName);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_ADD).withResource(TaskRes.RES_GROUP_NODE).withParameters(nodeAddCommand)
      .withStep("ADD", "Add group node", () -> groupAppService.addNodeToGroup(nodeAddCommand))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("AddNodeToGroup", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Batch add Group Node")
  @PostMapping("{groupName}/nodes/batch")
  public SmartMonResponse<TaskGroupVo> batchAddGroupNode(@RequestBody List<GroupNodeAddVo> nodeAddVos,
                                                    @PathVariable("groupName") String groupName) {
    List<TaskDescription> tasks = new ArrayList<>();
    for (GroupNodeAddVo nodeAddVo : nodeAddVos) {
      GroupNodeAddCommand nodeAddCommand = BeanConverter.copy(nodeAddVo, GroupNodeAddCommand.class);
      nodeAddCommand.setGroupName(groupName);
      final TaskDescription description = new TaskDescriptionBuilder()
        .withAction(TaskAct.ACT_ADD).withResource(TaskRes.RES_GROUP_NODE).withParameters(nodeAddCommand)
        .withStep("ADD", "Add group node", () -> groupAppService.addNodeToGroup(nodeAddCommand))
        .build();
      tasks.add(description);
    }
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("BatchAddNodeToGroup", tasks);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Delete Group Node")
  @DeleteMapping("{groupName}/nodes")
  public SmartMonResponse<TaskGroupVo> delGroupNode(@RequestBody GroupNodeDelVo nodeDelVo,
                                                    @PathVariable("groupName") String groupName) {
    GroupNodeDelCommand nodeDelCommand = BeanConverter.copy(nodeDelVo, GroupNodeDelCommand.class);
    nodeDelCommand.setGroupName(groupName);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_DEL).withResource(TaskRes.RES_GROUP_NODE).withParameters(nodeDelCommand)
      .withStep("DELETE", "Del group node", () ->  groupAppService.removeNodeFromGroup(nodeDelCommand))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("DelGroupNode", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Batch add luns")
  @PostMapping("{groupName}/luns/batch")
  public SmartMonResponse<TaskGroupVo> batchAddLuns(@RequestBody List<GroupLunAddVo> addVos,
                                                    @PathVariable("groupName") String groupName) {
    List<TaskDescription> tasks = new ArrayList<>();
    for (GroupLunAddVo addVo : addVos) {
      GroupLunAddCommand addCommand = new GroupLunAddCommand(addVo.getServiceIp(), addVo.getLunName(), groupName);
      final TaskDescription description = new TaskDescriptionBuilder()
        .withAction(TaskAct.ACT_ADD).withResource(TaskRes.RES_GROUP_LUN).withParameters(addCommand)
        .withStep("ADD", "Add group lun", () -> groupAppService.addLunToGroup(addCommand))
        .build();
      tasks.add(description);
    }
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("BatchAddLunToGroup", tasks);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

}
