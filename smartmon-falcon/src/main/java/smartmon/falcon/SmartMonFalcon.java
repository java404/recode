package smartmon.falcon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"smartmon"})
@MapperScan(basePackages = {"smartmon.taskmanager.mapper"})
public class SmartMonFalcon {
  public static void main(String[] args) {
    SpringApplication.run(SmartMonFalcon.class, args);
  }
}
