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
import smartmon.falcon.controller.vo.UserCreateVo;
import smartmon.falcon.controller.vo.UserUpdateVo;
import smartmon.falcon.user.TeamService;
import smartmon.falcon.user.User;
import smartmon.falcon.user.UserService;
import smartmon.falcon.user.command.UserCreateCommand;
import smartmon.falcon.user.command.UserUpdateCommand;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.BeanConverter;


@Api(tags = "users")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/users")
@RestController
public class UserController {
  @Autowired
  private UserService userService;
  @Autowired
  private TeamService teamService;

  /**
   * Get User List.
   */
  @ApiOperation("Get User List")
  @GetMapping
  public SmartMonResponse<List<User>> getUserList() {
    return new SmartMonResponse<>(userService.getUserList());
  }

  /**
   * Gets Users That Are Not In The Group.
   */
  @ApiOperation("Gets Users That Are Not In The Group")
  @GetMapping("{team-id}")
  public SmartMonResponse<List<User>> getUsersByTeamId(@PathVariable("teamId") Integer teamId) {
    final List<User> usersByTeamId = teamService.getUsersByTeamId(teamId);
    final List<User> userList = userService.getUserList();
    userList.removeAll(usersByTeamId);
    return new SmartMonResponse<>(usersByTeamId);
  }

  /**
   * Create User.
   */
  @ApiOperation("Create User")
  @PostMapping
  public SmartMonResponse createUser(@RequestBody UserCreateVo userCreateVo) {
    userService.createUser(BeanConverter.copy(userCreateVo, UserCreateCommand.class));
    return SmartMonResponse.OK;
  }

  /**
   * Update User.
   */
  @ApiOperation("Update User")
  @PutMapping("{user-id}")
  public SmartMonResponse updateUser(@PathVariable("user-id") Integer id, @RequestBody UserUpdateVo userUpdateVo) {
    final UserUpdateCommand userUpdateCommand = new UserUpdateCommand();
    userUpdateCommand.setId(id);
    userUpdateCommand.setCnName(userUpdateVo.getCnName());
    userUpdateCommand.setEmail(userUpdateVo.getEmail());
    userService.updateUser(userUpdateCommand);
    return SmartMonResponse.OK;
  }

  /**
   * Delete User.
   */
  @ApiOperation("Delete User")
  @DeleteMapping("{user-id}")
  public SmartMonResponse deleteUser(@PathVariable("user-id") Integer id) {
    userService.deleteUser(id);
    return SmartMonResponse.OK;
  }

  /**
   * Create User And Add To Group.
   */
  @ApiOperation("Create User And Add To Group")
  @PostMapping("create-to-group/{team-id}")
  public SmartMonResponse createUserAddToGroup(@PathVariable("team-id") Integer teamId,
                                               @RequestBody UserCreateVo userCreateVo) {
    userService.createUser(BeanConverter.copy(userCreateVo, UserCreateCommand.class));

    return null;
  }
}
