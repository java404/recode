package smartmon.smartstor.app.command;

import lombok.Data;

@Data
public class PoolAddCommand {
  private String serviceIp;
  private String diskName;
}
