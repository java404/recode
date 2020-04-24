package smartmon.smartstor.app.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.app.Command;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class DiskSyncCommand extends Command {
  private String serviceIp;
}
