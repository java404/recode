package smartmon.falcon.user;

import java.util.List;
import smartmon.falcon.user.command.TeamCreateCommand;
import smartmon.falcon.user.command.TeamUpdateCommand;
import smartmon.falcon.user.command.UserCreateCommand;

public interface TeamService {
  List<Team> getTeams();

  void createTeam(TeamCreateCommand teamCrateCommand);

  void updateTeam(TeamUpdateCommand teamUpdateCommand);

  void deleteTeam(Integer id);

  List<User> getUsersByTeamId(Integer teamId);

  void addUserToTeam(Integer teamId, Integer userId);

  void removeUserFromTeam(Integer teamId, Integer userId);

  void createUserToTeam(Integer teamId, UserCreateCommand createCommand);
}
