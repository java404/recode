package smartmon.gateway.security;

public interface AuthenticateService {
  void check(String token, String api, String method);
}
