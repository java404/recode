package smartmon.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"smartmon"})
public class SmartMonExamples {
  public static void main(String[] args) {
    SpringApplication.run(SmartMonExamples.class, args);
  }
}
