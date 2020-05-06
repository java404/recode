package smartmon.falcon.user;

import java.util.List;

public interface TeamService {
  List<Team> getTeams();

  void createTeam(String name, String resume);

  void updateTeam(Integer id, String resume);

  void deleteTeam(Integer id);

  List<User> getUsersByTeamId(Integer teamId);

  void addUserToTeam(Integer teamId, Integer userId);

  void removeUserFromTeam(Integer teamId, Integer userId);
}
