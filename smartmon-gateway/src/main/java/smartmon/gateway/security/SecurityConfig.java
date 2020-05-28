package smartmon.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {
  @Value("${smartmon.prefix:/gateway/api/v2}")
  private String apiPrefix;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private SecurityContextRepository securityContextRepository;

  private String[] getExcludePaths() {
    return new String[] {
      "/ui/**", "/repo/**", "/",
      "/swagger-ui.html**", "/webjars/**", "/swagger-resources/**",
      "/v2/api-docs", "/**/v2/api-docs",
      apiPrefix + "/passport/**"};
  }

  @Bean
  public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
    // Debug for disabel spring security.
    return http.authorizeExchange().pathMatchers("/**").permitAll().and().csrf().disable().build();
    //    http.csrf().disable()
    //      .formLogin().disable().httpBasic().disable()
    //      .authenticationManager(authenticationManager)
    //      .securityContextRepository(securityContextRepository)
    //      .authorizeExchange().pathMatchers(getExcludePaths()).permitAll()
    //      .pathMatchers("/**").authenticated();
    //
    //    http.exceptionHandling().authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
    //      swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    //    })).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
    //      swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
    //    }));
    //    return http.build();
  }
}
