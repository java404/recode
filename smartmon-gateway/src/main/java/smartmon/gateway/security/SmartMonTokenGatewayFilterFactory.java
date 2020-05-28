package smartmon.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmartMonTokenGatewayFilterFactory extends
    AbstractGatewayFilterFactory<SmartMonTokenGatewayFilterFactory.Config> {
  @Autowired
  private AuthenticateService authenticateService;

  public static class Config {
    public Config() {
      // NOP
    }
  }

  public SmartMonTokenGatewayFilterFactory() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      final ServerHttpRequest request = exchange.getRequest();
      final String token = request.getHeaders().getFirst("Authorization");
      log.debug("Filter Token: {}", token);
      authenticateService.check(token, request.getPath().value(), request.getMethodValue());
      return chain.filter(exchange.mutate().request(request).build());
    };
  }
}
