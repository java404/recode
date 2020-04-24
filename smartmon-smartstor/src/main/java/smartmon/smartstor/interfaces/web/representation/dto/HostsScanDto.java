package smartmon.smartstor.interfaces.web.representation.dto;

import lombok.Data;
import smartmon.smartstor.domain.model.enums.SysModeEnum;

@Data
public class HostsScanDto {
  private String serviceIp;
  private String hostId;
  private String hostname;
  private SysModeEnum sysMode;
  private String clusterName;
}
