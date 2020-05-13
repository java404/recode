package smartmon.falcon.user;

import java.util.List;
import smartmon.falcon.user.command.UserCreateCommand;
import smartmon.falcon.user.command.UserUpdateCommand;


public interface UserService {
  List<User> getUserList();

  void createUser(UserCreateCommand createCommand);

  void updateUser(UserUpdateCommand updateCommand);

  void deleteUser(Integer id);
}
