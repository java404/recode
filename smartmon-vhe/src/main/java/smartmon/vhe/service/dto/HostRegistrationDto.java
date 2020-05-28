package smartmon.vhe.service.dto;

import lombok.Data;

@Data
public class HostRegistrationDto {
  private String manageIp;
  private String sysUsername;
  private String sysPassword;
  private Integer sshPort;
  private String ipmiAddress;
  private String ipmiUsername;
  private String ipmiPassword;
}
