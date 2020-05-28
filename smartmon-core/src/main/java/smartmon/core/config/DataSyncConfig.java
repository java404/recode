package smartmon.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "smartmon.data.sync")
@Configuration
public class DataSyncConfig {
  private String agentStateCron = "0/10 * * * * ?";
  private String hostInfoCron = "0 0/1 * * * ?";
  private String ipmiInfoCron = "0 0/1 * * * ?";
}
