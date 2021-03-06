package smartmon.gateway.security;

import com.google.common.collect.Lists;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import smartmon.utilities.misc.StringItems;

@Slf4j
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {
  @Value("${smartmon.security.ignoreUrls}")
  private String ignoreUrls;

  @Value("${smartmon.security.enable:false}")
  private boolean securityEnable;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private SecurityContextRepository securityContextRepository;

  private String[] getExcludePaths() {
    final String[] urls = StringItems.parseArray(ignoreUrls);
    log.debug("Security Ignore Urls: {}", Arrays.toString(urls));
    return urls;
  }

  private Mono<AuthorizationDecision> checkAuthorities(Mono<Authentication> authentication, String... auth) {
    return authentication
      .filter(Authentication::isAuthenticated)
      .flatMapIterable(Authentication::getAuthorities)
      .map(GrantedAuthority::getAuthority)
      .collectList()
      .map(items -> items.containsAll(Lists.newArrayList(auth)))
      .map(AuthorizationDecision::new)
      .defaultIfEmpty(new AuthorizationDecision(false));
  }

  private void updatingSecurityPolicies(ServerHttpSecurity http) {
    http.authorizeExchange()
      .pathMatchers(HttpMethod.GET, "/gateway/api/v2/debug/**")
      .access((authentication, context) -> checkAuthorities(authentication, "admin", "user"))
      .anyExchange().authenticated();
  }

  @Bean
  public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
    log.debug("Initializing the security configuration");
    if (!securityEnable) {
      log.warn("SmartMon Security feature is disabled.");
      return http
        .authorizeExchange().pathMatchers("/**").permitAll()
        .and().csrf().disable().build();
    }

    log.debug("Security enabled.");
    http.exceptionHandling()
      .authenticationEntryPoint((exchange, exception) -> {
        return Mono.fromRunnable(() -> {
          exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        });
      }).accessDeniedHandler((exchange, exception) -> {
        return Mono.fromRunnable(() -> {
          exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        });
      }).and()
      .csrf().disable()
      .formLogin().disable()
      .httpBasic().disable()
      .authenticationManager(authenticationManager)
      .securityContextRepository(securityContextRepository)
      .authorizeExchange()
      .pathMatchers(getExcludePaths()).permitAll();
    updatingSecurityPolicies(http);
    return http.build();
  }
}
