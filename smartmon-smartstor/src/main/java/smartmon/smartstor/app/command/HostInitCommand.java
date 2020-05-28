package smartmon.smartstor.app.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.app.Command;

@Data
@EqualsAndHashCode(callSuper = false)
public class HostInitCommand extends Command {
  private String guid;
  private String hostId;
  private String listenIp;
}
