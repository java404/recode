package smartmon.smartstor.web.dto;

import lombok.Data;

@Data
public class SimplePoolDto {
  private String poolId;
  private String poolName;
  private Long lastHeartbeatTime;
  private Boolean actualState;
  private Long size;
  private Double exportPDirty;
}
