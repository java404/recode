package smartmon.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import smartmon.gateway.security.types.AuthRoles;
import smartmon.webtools.jwt.SmartMonJsonWebToken;
import smartmon.webtools.jwt.SmartMonToken;

@Slf4j
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
  @Autowired
  private SmartMonSecurity smartMonSecurity;

  @Value("${smartmon.security.internalToken:smartmon-internal}")
  private String internalToken;

  private boolean internalAccount(String authToken) {
    return internalToken.equals(authToken);
  }

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    final String authToken = authentication.getCredentials().toString();
    try {
      if (internalAccount(authToken)) {
        return Mono.just(new UsernamePasswordAuthenticationToken(
          authentication.getPrincipal(), null, AuthRoles.getInternalRole()));
      }

      final SmartMonJsonWebToken smartMonJsonWebToken = smartMonSecurity.getJsonWebToken();
      final SmartMonToken token = smartMonJsonWebToken.parse(authToken);
      if (!token.validateToken()) {
        log.warn("invalid token");
        return Mono.empty();
      }
      final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
        authentication.getPrincipal(), null,  AuthRoles.makeAuthorities(token.getRoles()));
      return Mono.just(auth);
    } catch (Exception error) {
      log.debug("authenticate error: ", error);
      return Mono.empty();
    }
  }
}
