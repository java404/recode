package smartmon.falcon.user;

import com.google.common.collect.Lists;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {
  @Override
  public List<Team> getTeams() {
    // TODO: call GET method api/v1/team
    return Lists.newArrayList();
  }

  @Override
  public void createTeam(String name, String resume) {
    // TODO: call POST method api/v1/team
  }

  @Override
  public void updateTeam(Integer id, String resume) {
    // TODO: call PUT method api/v1/team
  }

  @Override
  public void deleteTeam(Integer id) {
    // TODO: call DELETE method api/v1/team/{id}
  }

  @Override
  public List<User> getUsersByTeamId(Integer teamId) {
    // TODO: call GET method api/v1/team/t/{teamId}, and parse the values of key 'users'
    return Lists.newArrayList();
  }

  @Override
  public void addUserToTeam(Integer teamId, Integer userId) {
    // TODO: call PUT method api/v1/team
  }

  @Override
  public void removeUserFromTeam(Integer teamId, Integer userId) {
    // TODO: call PUT method api/v1/team
  }
}
