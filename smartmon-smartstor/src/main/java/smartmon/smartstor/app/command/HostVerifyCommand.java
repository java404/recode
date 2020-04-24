package smartmon.smartstor.app.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.app.Command;

@Data
@EqualsAndHashCode(callSuper = false)
public class HostVerifyCommand extends Command {
  private List<Host> hosts;

  @Data
  @AllArgsConstructor
  public static class Host {
    private String serviceIp;
    private String listenIp;
    private String hostId;
  }
}
