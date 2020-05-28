package smartmon.core.hosts;

import java.util.List;

import smartmon.core.hosts.types.HostAddCommand;
import smartmon.core.hosts.types.HostConfigCommand;
import smartmon.core.hosts.types.SmartMonHost;

public interface HostsService {
  List<SmartMonHost> getAll();

  SmartMonHost getHostById(String hostUuid);

  SmartMonHost addHost(HostAddCommand hostAddCommand);

  SmartMonHost configHost(String hostUuid, HostConfigCommand hostConfigCommand);

  boolean isServer(String ip);
}
