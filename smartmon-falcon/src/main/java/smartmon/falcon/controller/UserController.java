package smartmon.falcon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.falcon.controller.vo.UserCreateVo;
import smartmon.falcon.controller.vo.UserUpdateVo;
import smartmon.falcon.user.TeamService;
import smartmon.falcon.user.User;
import smartmon.falcon.user.UserService;
import smartmon.falcon.user.command.UserCreateCommand;
import smartmon.falcon.user.command.UserUpdateCommand;
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


@Api(tags = "users")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/users")
@RestController
public class UserController {
  @Autowired
  private UserService userService;
  @Autowired
  private TeamService teamService;
  @Autowired
  private TaskManagerService taskManagerService;

  /**
   * Get User List.
   */
  @ApiOperation("Get User List")
  @SmartMonPageParams
  @GetMapping
  public SmartMonResponse<Page<User>> getUserList(ServerHttpRequest request) {
    return new SmartMonPageResponseBuilder<>(userService.getUserList(),
      request, "id").build();
  }

  /**
   * Gets Users That Are Not In The Group.
   */
  @ApiOperation("Gets Users That Are Not In The Group")
  @GetMapping("{team-id}")
  public SmartMonResponse<List<User>> getUsersByTeamId(@PathVariable("team-id") Integer teamId) {
    final List<User> usersByTeamId = teamService.getUsersByTeamId(teamId);
    final List<User> userList = userService.getUserList();
    userList.removeAll(usersByTeamId);
    return new SmartMonResponse<>(userList);
  }

  /**
   * Create User.
   */
  @ApiOperation("Create User")
  @PostMapping
  public SmartMonResponse<TaskGroupVo> createUser(@RequestBody UserCreateVo userCreateVo) {
    final UserCreateCommand userCreateCommand = BeanConverter.copy(userCreateVo, UserCreateCommand.class);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_CREATE).withResource(TaskRes.RES_FALCON_USER).withParameters(userCreateCommand)
      .withStep("CREATE", "Create user", () -> userService.createUser(userCreateCommand))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("CreateUser", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  /**
   * Update User.
   */
  @ApiOperation("Update User")
  @PutMapping("{user-id}")
  public SmartMonResponse<TaskGroupVo> updateUser(@PathVariable("user-id") Integer id,
                                                  @RequestBody UserUpdateVo userUpdateVo) {
    final UserUpdateCommand userUpdateCommand = new UserUpdateCommand();
    userUpdateCommand.setId(id);
    userUpdateCommand.setCnName(userUpdateVo.getCnName());
    userUpdateCommand.setEmail(userUpdateVo.getEmail());
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_UPDATE).withResource(TaskRes.RES_FALCON_USER).withParameters(userUpdateCommand)
      .withStep("UPDATE", "Update user", () -> userService.updateUser(userUpdateCommand))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("UpdateUser", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  /**
   * Delete User.
   */
  @ApiOperation("Delete User")
  @DeleteMapping("{user-id}")
  public SmartMonResponse<TaskGroupVo> deleteUser(@PathVariable("user-id") Integer id) {
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_DEL).withResource(TaskRes.RES_FALCON_USER).withParameters(id)
      .withStep("DELETE", "Delete user", () -> userService.deleteUser(id))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("DeleteUser", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }
}
