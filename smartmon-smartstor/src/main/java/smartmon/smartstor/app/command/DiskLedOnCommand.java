package smartmon.smartstor.app.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.app.Command;

@Data
@EqualsAndHashCode(callSuper = false)
public class DiskLedOnCommand extends Command {
  private String serviceIp;
  private String cesAddr;
}
