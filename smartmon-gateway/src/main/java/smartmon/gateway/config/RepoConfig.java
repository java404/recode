package smartmon.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Slf4j
@Configuration
public class RepoConfig {
  @Value("${smartmon.repo.root:/opt/smartmon/smartmon-injectors/repo/}")
  private String repoRoot;

  private Resource makeRepoResource() {
    final String root = repoRoot.endsWith("/") ? repoRoot : repoRoot + "/";
    log.info("Repo resource root: {}", root);
    return new FileSystemResource(root);
  }

  @Bean
  public RouterFunction<ServerResponse> repoResourceRouter() {
    return RouterFunctions.resources("/repo/**", makeRepoResource());
  }
}
