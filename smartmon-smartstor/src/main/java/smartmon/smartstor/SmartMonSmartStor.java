package smartmon.smartstor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = {"smartmon"})
@EnableAsync
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = {"smartmon.smartstor.infra.persistence.mapper",
  "smartmon.taskmanager.mapper"})
public class SmartMonSmartStor {
  public static void main(String[] args) {
    SpringApplication.run(SmartMonSmartStor.class, args);
  }
}
