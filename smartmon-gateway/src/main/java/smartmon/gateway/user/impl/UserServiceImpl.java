package smartmon.gateway.user.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import smartmon.gateway.user.UserPrincipal;
import smartmon.gateway.user.UserService;
import smartmon.webtools.auth.SmartMonAuthService;
import smartmon.webtools.auth.SmartMonToken;

@Service
public class UserServiceImpl implements UserService {
  private UserPrincipal defaultUser;

  @Autowired
  private SmartMonAuthService authService;

  @PostConstruct
  void init() {
    final List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("USER"));
    authorities.add(new SimpleGrantedAuthority("ADMIN"));
    defaultUser = new UserPrincipal("root", "root", authorities);
  }

  @Override
  public UserPrincipal findByUsername(String username) {
    return defaultUser;
  }

  @Override
  public String generateToken(UserPrincipal userPrincipal) {
    final SmartMonToken smartMonToken = authService.getSmartMonToke();
    final Map<String, Object> claims = new HashMap<>();
    claims.put("username", userPrincipal.getUsername());
    claims.put("role", userPrincipal.getAuthorities());
    return smartMonToken.generateToken("smartmon", claims);
  }
}
