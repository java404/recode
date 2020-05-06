package smartmon.falcon.user;

public interface UserService {
  void createUser(String name, String cnName, String email);

  void updateUser(Integer id, String cnName, String email);
}
