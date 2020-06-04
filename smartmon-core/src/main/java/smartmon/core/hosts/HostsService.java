package smartmon.core.hosts;

import java.util.List;

import smartmon.core.hosts.types.HostAddCommand;
import smartmon.core.hosts.types.HostConfigCommand;
import smartmon.core.hosts.types.MonitorNetInterfaceVo;

public interface HostsService {
  List<SmartMonHost> getAll();

  SmartMonHost getHostById(String hostUuid);

  SmartMonHost addHost(HostAddCommand hostAddCommand);

  SmartMonHost configHost(String hostUuid, HostConfigCommand hostConfigCommand);

  MonitorNetInterfaceVo getMonitorNetInterfaces(String hostUuid);

  void configMonitorNetInterfaces(String hostUuid, List<String> monitorNetInterfaces);

  List<String> getMonitorNetInterfaces(SmartMonHost host);

  boolean isServer(String ip);
}
