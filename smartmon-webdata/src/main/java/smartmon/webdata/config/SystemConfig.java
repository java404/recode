package smartmon.webdata.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class SystemConfig {
  @Value("${smartmon.system.server.address:}")
  private String serverAddress;
  @Value("${smartmon.system.server.port:80}")
  private Integer serverPort;
  @Value("${smartmon.system.monitor.agent.port:}")
  private Integer agentPort;
  @Value("${smartmon.system.monitor.collector.port:}")
  private Integer collectorPort;
  @Value("${smartmon.system.monitor.injector.port:1989}")
  private Integer injectorPort;
}
