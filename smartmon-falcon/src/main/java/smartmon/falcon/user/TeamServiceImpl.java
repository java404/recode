package smartmon.falcon.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.config.FalconApiComponent;
import smartmon.falcon.remote.types.team.FalconTeam;
import smartmon.falcon.remote.types.team.FalconTeamCreateParam;
import smartmon.falcon.remote.types.team.FalconTeamInfo;
import smartmon.falcon.remote.types.team.FalconTeamUpdateParam;
import smartmon.falcon.remote.types.team.FalconTeamUserInfo;
import smartmon.falcon.remote.types.user.FalconUser;
import smartmon.falcon.remote.types.user.FalconUserCreateParam;
import smartmon.falcon.remote.types.user.FalconUserCreateResponse;
import smartmon.falcon.user.command.TeamCreateCommand;
import smartmon.falcon.user.command.TeamUpdateCommand;
import smartmon.falcon.user.command.UserCreateCommand;
import smartmon.utilities.misc.BeanConverter;

@Service
public class TeamServiceImpl implements TeamService {

  @Autowired
  private FalconApiComponent falconApiComponent;

  @Override
  public List<Team> getTeams() {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final List<FalconTeamInfo> falconTeamInfos = falconClient.listTeams(falconApiComponent.getApiToken());
    final List<FalconTeam> falconTeams = falconTeamInfos.stream()
      .map(FalconTeamInfo::getTeam).collect(Collectors.toList());
    return ListUtils.emptyIfNull(BeanConverter.copy(falconTeams, Team.class));
  }

  @Override
  public void createTeam(TeamCreateCommand createCommand) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconTeamCreateParam teamCreateParam = BeanConverter.copy(createCommand, FalconTeamCreateParam.class);
    falconClient.createTeam(teamCreateParam, falconApiComponent.getApiToken());
  }

  @Override
  public void updateTeam(TeamUpdateCommand updateCommand) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconTeamUserInfo teamUserInfo = falconClient.getTeamUserInfoByTeamId(
      updateCommand.getId(), falconApiComponent.getApiToken());
    final FalconTeamUpdateParam teamUpdateParam = BeanConverter.copy(updateCommand, FalconTeamUpdateParam.class);
    teamUpdateParam.setName(teamUserInfo.getName());
    teamUpdateParam.setUsers(teamUserInfo.getUsers().stream().map(FalconUser::getId).collect(Collectors.toList()));
    falconClient.updateTeam(teamUpdateParam, falconApiComponent.getApiToken());
  }

  @Override
  public void deleteTeam(Integer id) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    falconClient.deleteTeam(id, falconApiComponent.getApiToken());
  }

  @Override
  public List<User> getUsersByTeamId(Integer teamId) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final List<FalconUser> falconUsers = falconClient.getUsersByTeamId(teamId, falconApiComponent.getApiToken());
    return ListUtils.emptyIfNull(BeanConverter.copy(falconUsers, User.class));
  }

  @Override
  public void addUserToTeam(Integer teamId, Integer userId) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconTeamUserInfo teamUserInfo = falconClient.getTeamUserInfoByTeamId(
      teamId, falconApiComponent.getApiToken());
    final List<FalconUser> users = teamUserInfo.getUsers();
    List<Integer> userIds = new ArrayList<>();
    for (FalconUser user : users) {
      userIds.add(user.getId());
    }
    userIds.add(userId);
    FalconTeamUpdateParam teamUpdateParam = new FalconTeamUpdateParam();
    teamUpdateParam.setId(teamId);
    teamUpdateParam.setName(teamUserInfo.getName());
    teamUpdateParam.setResume(teamUserInfo.getResume());
    teamUpdateParam.setUsers(userIds);
    falconClient.updateTeam(teamUpdateParam, falconApiComponent.getApiToken());
  }

  @Override
  public void removeUserFromTeam(Integer teamId, Integer userId) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconTeamUserInfo teamUserInfo = falconClient.getTeamUserInfoByTeamId(
      teamId, falconApiComponent.getApiToken());
    final List<FalconUser> users = teamUserInfo.getUsers();
    List<Integer> userIds = new ArrayList<>();
    for (FalconUser user : users) {
      userIds.add(user.getId());
    }
    userIds.remove(userId);
    FalconTeamUpdateParam teamUpdateParam = new FalconTeamUpdateParam();
    teamUpdateParam.setId(teamId);
    teamUpdateParam.setName(teamUserInfo.getName());
    teamUpdateParam.setResume(teamUserInfo.getResume());
    teamUpdateParam.setUsers(userIds);
    falconClient.updateTeam(teamUpdateParam, falconApiComponent.getApiToken());
  }

  @Override
  public void createUserToTeam(Integer teamId, UserCreateCommand createCommand) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconUserCreateResponse userCreateResponse = falconClient.createUser(BeanConverter.copy(createCommand,
      FalconUserCreateParam.class), falconApiComponent.getApiToken());
    addUserToTeam(teamId, Integer.parseInt(userCreateResponse.getId()));
  }
}
