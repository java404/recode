package smartmon.falcon.remote.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "smartmon.falcon")
@Getter
@Setter
public class FalconApiConfig {
  private String address;
  private Integer requestPort;
  private String pushPort;
}
