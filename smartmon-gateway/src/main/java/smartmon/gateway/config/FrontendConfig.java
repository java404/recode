package smartmon.gateway.config;

import java.net.MalformedURLException;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileUrlResource;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Slf4j
@Configuration
public class FrontendConfig {
  @Value("${smartmon.frontend.root:/opt/smartmon/smartmon-frontend-vhe/}")
  private String frontendRoot;

  private FileUrlResource makeFrontendResource() throws MalformedURLException {
    final String root =  frontendRoot.endsWith("/") ? frontendRoot : frontendRoot + "/";
    log.info("Frontend UI resource root: {}", root);
    return new FileUrlResource(root);
  }

  @Bean
  public RouterFunction<ServerResponse> staticResourceRouter() throws MalformedURLException {
    return RouterFunctions.resources("/ui/**", makeFrontendResource());
  }

  @Bean
  RouterFunction<ServerResponse> routerFunction() {
    return RouterFunctions.route(RequestPredicates.GET("/"),
      req -> ServerResponse.permanentRedirect(URI.create("/ui/index.html")).build());
  }
}
