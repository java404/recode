package smartmon.gateway.config;

import com.google.common.base.Strings;
import java.net.URI;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import smartmon.utilities.misc.FileUtilities;

@Slf4j
@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {
  private static final long MIN_CACHE_MINUTES = 3;

  @Value("${smartmon.frontend.root:/opt/smartmon/smartmon-frontend-vhe/}")
  private String frontendRoot;

  @Value("${smartmon.frontend.cacheMinutes:180}")
  private long frontendCacheMinutes;

  @Value("${smartmon.repo.root:/opt/smartmon/smartmon-injectors/repo/}")
  private String repoRoot;

  private Resource makeRepoResource() {
    log.info("Repo resource root: {}", Strings.nullToEmpty(repoRoot));
    return new FileSystemResource(FileUtilities.makeDirName(repoRoot));
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/ui/**")
      .addResourceLocations("/ui", String.format("file:%s", FileUtilities.makeDirName(frontendRoot)))
      .setCacheControl(CacheControl.maxAge(Duration.ofMinutes(Math.max(frontendCacheMinutes, MIN_CACHE_MINUTES))));

    registry.addResourceHandler("/swagger-ui.html**")
      .addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**")
      .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  @Bean
  RouterFunction<ServerResponse> routerFunction() {
    return RouterFunctions.route(RequestPredicates.GET("/"),
      req -> ServerResponse.permanentRedirect(URI.create("/ui/index.html")).build());
  }

  @Bean
  public RouterFunction<ServerResponse> repoResourceRouter() {
    return RouterFunctions.resources("/repo/**", makeRepoResource());
  }
}
