package smartmon.gateway;

import java.net.MalformedURLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileUrlResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@EnableDiscoveryClient
@SpringBootApplication
public class SmartMonGateway {
  public static void main(String[] args) {
    SpringApplication.run(SmartMonGateway.class, args);
  }

  @Bean
  RouterFunction<ServerResponse> staticResourceRouter() throws MalformedURLException {
    return RouterFunctions.resources("/ui/**", new FileUrlResource("/usr/share/nginx/html/ui/"));
  }
}
