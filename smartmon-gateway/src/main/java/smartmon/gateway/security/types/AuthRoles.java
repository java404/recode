package smartmon.gateway.security.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthRoles {
  private static final String[] ROLES = {"ADMIN", "USER", "INTERNAL"};
  private static final Map<String, GrantedAuthority> roleAuthorities;
  private static final List<GrantedAuthority> INTERNAL_ROLE;
  private static final GrantedAuthority DEFAULT_ROLE_AUTH;

  static {
    roleAuthorities = new HashMap<>();
    for (String role : ROLES) {
      roleAuthorities.put(role, makeAuthority(role));
    }
    DEFAULT_ROLE_AUTH = roleAuthorities.get("USER");
    INTERNAL_ROLE = new ArrayList<>(roleAuthorities.values());
  }

  private static GrantedAuthority makeAuthority(String role) {
    return new SimpleGrantedAuthority("ROLE_" + role);
  }

  public static List<GrantedAuthority> getInternalRole() {
    return INTERNAL_ROLE;
  }

  public static List<GrantedAuthority> makeAuthorities(List<String> roles) {
    final List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("admin"));
    authorities.add(new SimpleGrantedAuthority("user"));

    //    for (final String role : ListUtils.emptyIfNull(roles)) {
    //      authorities.add(roleAuthorities.getOrDefault(role, DEFAULT_ROLE_AUTH));
    //    }
    return authorities;
  }
}
