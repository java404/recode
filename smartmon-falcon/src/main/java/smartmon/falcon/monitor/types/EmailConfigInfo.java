package smartmon.falcon.monitor.types;

import lombok.Data;

@Data
public class EmailConfigInfo {
  private String address;
  private String senderEmail;
  private String port;
  private String username;
  private String password;
  private Boolean enabled;
  private Boolean auth;
}
