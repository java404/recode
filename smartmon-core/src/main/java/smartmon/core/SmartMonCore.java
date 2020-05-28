package smartmon.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@ComponentScan(basePackages = {"smartmon"})
@MapperScan(basePackages = {"smartmon.taskmanager.mapper", "smartmon.core.mapper"})
@tk.mybatis.spring.annotation.MapperScan(basePackages = {"smartmon.core.hosts.mapper"})
public class SmartMonCore {
  public static void main(String[] args) {
    SpringApplication.run(SmartMonCore.class, args);
  }
}
