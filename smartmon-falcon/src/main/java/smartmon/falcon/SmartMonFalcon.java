package smartmon.falcon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"smartmon"})
public class SmartMonFalcon {
  public static void main(String[] args) {
    SpringApplication.run(SmartMonFalcon.class, args);
  }
}
