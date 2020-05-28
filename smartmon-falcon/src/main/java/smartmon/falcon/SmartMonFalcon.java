package smartmon.falcon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"smartmon"})
@MapperScan(basePackages = {"smartmon.taskmanager.mapper","smartmon.falcon.mapper"})
public class SmartMonFalcon {
  public static void main(String[] args) {
    SpringApplication.run(SmartMonFalcon.class, args);
  }
}
