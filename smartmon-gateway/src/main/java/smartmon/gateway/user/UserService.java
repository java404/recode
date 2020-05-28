package smartmon.gateway.user;

public interface UserService {
  UserPrincipal findByUsername(String username);

  String generateToken(UserPrincipal userPrincipal);
}
