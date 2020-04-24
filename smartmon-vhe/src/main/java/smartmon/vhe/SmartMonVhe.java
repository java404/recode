package smartmon.vhe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = {"smartmon.vhe.service.feign"})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"smartmon"})
public class SmartMonVhe {
  public static void main(String[] args) {
    SpringApplication.run(SmartMonVhe.class, args);
  }
}
