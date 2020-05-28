package smartmon.smartstor.app.command;

import lombok.Data;

@Data
public class PoolSizeCommand {
  private String serviceIp;
  private String poolName;
  private Long size;
}
