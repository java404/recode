package smartmon.gateway.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    final String authToken = authentication.getCredentials().toString();
    final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
      authentication.getPrincipal(), null,  null);
    return Mono.just(auth);
  }
}
