package smartmon.smartstor.infra.remote.types.lun;

import lombok.Data;

@Data
public class PbDataLunIpAddr {
  private String ip;
  private Integer port;
}
