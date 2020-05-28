package smartmon.smartstor.app.command;

import lombok.Data;

@Data
public class PoolSyncLevelCommand {
  private String serviceIp;
  private String poolName;
  private Integer syncLevel;

}
