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
import smartmon.falcon.controller.vo.TeamCreateVo;
import smartmon.falcon.controller.vo.TeamUpdateVo;
import smartmon.falcon.controller.vo.UserCreateVo;
import smartmon.falcon.user.Team;
import smartmon.falcon.user.TeamService;
import smartmon.falcon.user.User;
import smartmon.falcon.user.command.TeamCreateCommand;
import smartmon.falcon.user.command.TeamUpdateCommand;
import smartmon.falcon.user.command.UserCreateCommand;
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

@Api(tags = "teams")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/teams")
@RestController
public class TeamController {
  @Autowired
  private TeamService teamService;
  @Autowired
  private TaskManagerService taskManagerService;

  /**
   * Get Team List.
   */
  @ApiOperation("Get Team List")
  @SmartMonPageParams
  @GetMapping
  public SmartMonResponse<Page<Team>> getTeamList(ServerHttpRequest request) {
    return new SmartMonPageResponseBuilder<>(teamService.getTeams(),
      request, "id").build();
  }

  /**
   * Create Team.
   */
  @ApiOperation("Create Team")
  @PostMapping
  public SmartMonResponse<TaskGroupVo> createTeam(@RequestBody TeamCreateVo teamCreateVo) {
    final TeamCreateCommand teamCreateCommand = BeanConverter.copy(teamCreateVo, TeamCreateCommand.class);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_CREATE).withResource(TaskRes.RES_FALCON_TEAM).withParameters(teamCreateCommand)
      .withStep("CREATE", "Create team", () -> teamService.createTeam(teamCreateCommand))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("CreateTeam", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  /**
   * Update Team.
   */
  @ApiOperation("Update Team")
  @PutMapping("{team-id}")
  public SmartMonResponse<TaskGroupVo> updateTeam(@PathVariable("team-id") Integer id,
                                                  @RequestBody TeamUpdateVo teamUpdateVo) {
    TeamUpdateCommand teamUpdateCommand = new TeamUpdateCommand();
    teamUpdateCommand.setId(id);
    teamUpdateCommand.setResume(teamUpdateVo.getResume());
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_UPDATE).withResource(TaskRes.RES_FALCON_TEAM).withParameters(teamUpdateCommand)
      .withStep("UPDATE", "Update team", () -> teamService.updateTeam(teamUpdateCommand))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("UpdateTeam", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  /**
   * Delete Team.
   */
  @ApiOperation("Delete Team")
  @DeleteMapping("{team-id}")
  public SmartMonResponse<TaskGroupVo> deleteTeam(@PathVariable("team-id") Integer id) {
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_DEL).withResource(TaskRes.RES_FALCON_TEAM).withParameters(id)
      .withStep("DELETE", "Delete team", () -> teamService.deleteTeam(id))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("DeleteTeam", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  /**
   * Get Team By Team Id.
   */
  @ApiOperation("Get Users By Team Id")
  @GetMapping("{team-id}/users")
  public SmartMonResponse<List<User>> getUsersByTeamId(@PathVariable("team-id") Integer id) {
    final List<User> usersByTeamId = teamService.getUsersByTeamId(id);
    return new SmartMonResponse<>(usersByTeamId);
  }

  /**
   * Add User To Team.
   */
  @ApiOperation("Add User To Team")
  @PostMapping("{team-id}/user/{user-id}")
  public SmartMonResponse<TaskGroupVo> addUserToTeam(@PathVariable("team-id") Integer teamId,
                                                     @PathVariable("user-id") Integer userId) {
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_PATCH).withResource(TaskRes.RES_FALCON_TEAM).withParameters(userId)
      .withStep("ADD", "ADD user To team", () -> teamService.addUserToTeam(teamId, userId))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("AddUserToTeam", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  /**
   * Remove User From Team.
   */
  @ApiOperation("Remove User From Team")
  @DeleteMapping("{team-id}/user/{user-id}")
  public SmartMonResponse<TaskGroupVo> removeUserFromTeam(@PathVariable("team-id") Integer teamId,
                                                          @PathVariable("user-id") Integer userId) {
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_PATCH).withResource(TaskRes.RES_FALCON_TEAM).withParameters(userId)
      .withStep("REMOVE", "Remove user From team", () -> teamService.removeUserFromTeam(teamId, userId))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("RemoveUserFromTeam", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  /**
   * Create User And Add To Group.
   */
  @ApiOperation("Create User And Add To Group")
  @PostMapping("{team-id}/user")
  public SmartMonResponse<TaskGroupVo> createToTeam(@PathVariable("team-id") Integer teamId,
                                                    @RequestBody UserCreateVo userCreateVo) {
    final UserCreateCommand userCreateCommand = BeanConverter.copy(userCreateVo, UserCreateCommand.class);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_PATCH).withResource(TaskRes.RES_FALCON_TEAM).withParameters(userCreateVo)
      .withStep("PATCH", "Create user To team", () -> teamService.createUserToTeam(teamId, userCreateCommand))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("CreateUserToTeam", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }
}
