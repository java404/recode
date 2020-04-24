package smartmon.core.hosts;

import java.util.List;

import smartmon.core.hosts.types.HostAddCommand;
import smartmon.core.hosts.types.SmartMonHost;

public interface HostsService {
  List<SmartMonHost> getAll();

  List<SmartMonHost> addHosts(List<HostAddCommand> hostAddCommands);
}
