package smartmon.vhe.service.dto;

import lombok.Data;

@Data
public class StorageHostInitDto {
  private String guid;
  private String listenIp;
  private String hostId;
}
