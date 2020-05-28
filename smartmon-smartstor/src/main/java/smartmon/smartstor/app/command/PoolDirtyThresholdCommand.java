package smartmon.smartstor.app.command;

import lombok.Data;

@Data
public class PoolDirtyThresholdCommand {
  private String serviceIp;
  private String poolName;
  private Integer threshLower;
  private Integer threshUpper;
}
