package smartmon.falcon.user;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Override
  public void createUser(String name, String cnName, String email) {
    // TODO: call POST method api/v1/user/create
  }

  @Override
  public void updateUser(Integer id, String cnName, String email) {
    // TODO: call PUT method api/v1/admin/change_user_profile
  }
}
