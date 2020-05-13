package smartmon.core.hosts.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartmon.core.hosts.HostsService;
import smartmon.core.hosts.types.HostAddCommand;
import smartmon.core.hosts.types.SmartMonHost;
import smartmon.core.mapper.SmartMonHostMapper;

@Service
public class HostsServiceImpl implements HostsService {
  @Autowired
  private SmartMonHostMapper smartMonHostMapper;

  @Override
  public List<SmartMonHost> getAll() {
    return smartMonHostMapper.selectAll();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<SmartMonHost> addHosts(List<HostAddCommand> hostAddCommands) {
    List<SmartMonHost> smartMonHosts = hostAddCommands.stream().map(this::addHost).collect(Collectors.toList());
    saveSmartMonHosts(smartMonHosts);
    return smartMonHosts;
  }

  private SmartMonHost addHost(HostAddCommand hostAddCommand) {
    //TODO: need improve, add other host info
    List<SmartMonHost> smartMonHosts = smartMonHostMapper.selectAll();
    SmartMonHost smartMonHost = new SmartMonHost();
    smartMonHost.setHostUuid(getExistedHostUuid(smartMonHosts, hostAddCommand));
    smartMonHost.setManageIp(hostAddCommand.getManageIp());
    smartMonHost.setSysUsername(hostAddCommand.getSysUsername());
    smartMonHost.setSysPassword(hostAddCommand.getSysPassword());
    return smartMonHost;
  }

  private String getExistedHostUuid(List<SmartMonHost> smartMonHosts, HostAddCommand hostAddCommand) {
    //TODO: need improve, judge all ips
    return smartMonHosts.stream()
      .filter(smartMonHost -> smartMonHost.getManageIp().equals(hostAddCommand.getManageIp()))
      .map(SmartMonHost::getHostUuid)
      .findFirst().orElse(null);
  }

  private void saveSmartMonHosts(List<SmartMonHost> smartMonHosts) {
    for (SmartMonHost host : smartMonHosts) {
      saveSmartMonHost(host);
    }
  }

  private void saveSmartMonHost(SmartMonHost host) {
    if (host.getHostUuid() == null) {
      host.setHostUuid(UUID.randomUUID().toString());
      smartMonHostMapper.insert(host);
    } else {
      smartMonHostMapper.updateByPrimaryKey(host);
    }
  }
}
