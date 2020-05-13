package smartmon.vhe.service.feign.types;

import lombok.Data;

@Data
public class SmartMonHostAddParam {
  private String manageIp;
  private String sysUsername;
  private String sysPassword;
}
