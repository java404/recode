package smartmon.smartstor.app.command;

import lombok.Data;

@Data
public class PoolDeleteCommand {
  private String serviceIp;
  private String poolName;
}
