package smartmon.examples;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"smartmon.taskmanager.mapper", "smartmon.examples"})
@ComponentScan(basePackages = {"smartmon"})
public class SmartMonExamples {
  public static void main(String[] args) {
    SpringApplication.run(SmartMonExamples.class, args);
  }
}
