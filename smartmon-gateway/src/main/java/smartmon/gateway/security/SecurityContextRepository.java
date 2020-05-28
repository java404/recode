package smartmon.gateway.security;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Override
  public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange exchange) {
    final ServerHttpRequest request = exchange.getRequest();
    final String authToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (Strings.isNullOrEmpty(authToken)) {
      return Mono.empty();
    }
    final Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
    return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
  }
}
