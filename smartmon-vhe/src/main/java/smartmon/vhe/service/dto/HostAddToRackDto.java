package smartmon.vhe.service.dto;

import lombok.Data;

@Data
public class HostAddToRackDto {
  private String hostUuid;
  private String idcName;
  private Integer size;
}
