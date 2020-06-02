package smartmon.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"smartmon"})
public class SmartMonGateway {
  public static void main(String[] args) {
    SpringApplication.run(SmartMonGateway.class, args);
  }
}
