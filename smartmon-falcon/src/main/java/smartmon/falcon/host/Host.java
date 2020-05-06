package smartmon.falcon.host;

import lombok.Data;

@Data
public class Host {
  private Integer id;
  private String hostname;
  private String ip;
  private String agentVersion;
  private String pluginVersion;
}
