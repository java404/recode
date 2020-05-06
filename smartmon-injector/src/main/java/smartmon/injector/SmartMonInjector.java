package smartmon.injector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"smartmon"})
public class SmartMonInjector {
  public static void main(String[] args) {
    SpringApplication.run(SmartMonInjector.class, args);
  }
}
