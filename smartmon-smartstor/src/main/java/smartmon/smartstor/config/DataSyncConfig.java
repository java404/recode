package smartmon.smartstor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "smartmon.data")
public class DataSyncConfig {
  private Boolean sync = true;
  private DataSyncConfigDetail smartstor;

  @Getter
  @Setter
  public static class DataSyncConfigDetail {
    private Boolean sync = true;
    private String cron;
    private Integer timeout;
  }
}
