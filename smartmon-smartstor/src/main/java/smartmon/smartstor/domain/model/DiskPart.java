package smartmon.smartstor.domain.model;

import lombok.Data;

@Data
public class DiskPart {
  private String devName; // "dev_name": "/dev/sdf1",
  private Long size; // "size": 1171058655,
  private Long lastHeartbeatTime; // "last_heartbeat_time": 1542093024,
  private Boolean actualState; // "actual_state": true,
  private Integer diskPart; // "disk_part": 1
}
