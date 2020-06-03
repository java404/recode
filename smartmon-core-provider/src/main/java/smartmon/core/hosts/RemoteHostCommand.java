package smartmon.core.hosts;

import lombok.Getter;
import lombok.Setter;
import smartmon.utilities.misc.TargetHost;

@Getter
@Setter
public class RemoteHostCommand {
  private String address;
  private Integer port;
  private String username;
  private String password;

  public TargetHost toTargetHost() {
    return TargetHost.builder(address, port).username(username).password(password).build();
  }
}
