package smartmon.falcon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import smartmon.falcon.user.Team;
import smartmon.falcon.user.TeamService;
import smartmon.falcon.user.User;
import smartmon.falcon.user.command.TeamCreateCommand;
import smartmon.falcon.user.command.TeamUpdateCommand;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.BeanConverter;

@Api(tags = "teams")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/teams")
@RestController
public class TeamController {
  @Autowired
  private TeamService teamService;

  /**
   * Get Team List.
   */
  @ApiOperation("Get Team List")
  @GetMapping
  public SmartMonResponse<List<Team>> getTeamList() {
    return new SmartMonResponse<>(teamService.getTeams());
  }

  /**
   * Create Team.
   */
  @ApiOperation("Create Team")
  @PostMapping
  public SmartMonResponse createTeam(@RequestBody TeamCreateVo teamCreateVo) {
    teamService.createTeam(BeanConverter.copy(teamCreateVo, TeamCreateCommand.class));
    return SmartMonResponse.OK;
  }

  /**
   * Update Team.
   */
  @ApiOperation("Update Team")
  @PutMapping("{team-id}")
  public SmartMonResponse updateTeam(@PathVariable("team-id") Integer id, @RequestBody TeamUpdateVo teamUpdateVo) {
    TeamUpdateCommand teamUpdateCommand = new TeamUpdateCommand();
    teamUpdateCommand.setId(id);
    teamUpdateCommand.setResume(teamUpdateVo.getResume());
    teamService.updateTeam(teamUpdateCommand);
    return SmartMonResponse.OK;
  }

  /**
   * Delete Team.
   */
  @ApiOperation("Delete Team")
  @DeleteMapping("{team-id}")
  public SmartMonResponse deleteTeam(@PathVariable("team-id") Integer id) {
    teamService.deleteTeam(id);
    return SmartMonResponse.OK;
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
  public SmartMonResponse addUserToTeam(@PathVariable("team-id") Integer teamId,
                                        @PathVariable("user-id") Integer userId) {
    teamService.addUserToTeam(teamId, userId);
    return SmartMonResponse.OK;
  }

  /**
   * Remove User From Team.
   */
  @ApiOperation("Remove User From Team")
  @DeleteMapping("{team-id}/user/{user-id}")
  public SmartMonResponse removeUserFromTeam(@PathVariable("team-id") Integer teamId,
                                             @PathVariable("user-id") Integer userId) {
    teamService.removeUserFromTeam(teamId, userId);
    return SmartMonResponse.OK;
  }
}
