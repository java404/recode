package smartmon.smartstor.app.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.app.Command;

@Data
@EqualsAndHashCode(callSuper = false)
public class DiskAddCommand extends Command {
  private String serviceIp;
  private String devName;
  private Integer partitionCount;
  private String diskType;
}
