package smartmon.injector;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"smartmon"})
@MapperScan(basePackages = {"smartmon.taskmanager.mapper"})
public class SmartMonInjector {
  public static void main(String[] args) {
    SpringApplication.run(SmartMonInjector.class, args);
  }
}
