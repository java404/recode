package smartmon.core.hosts.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.config.SmartMonErrno;
import smartmon.core.hosts.HostsService;
import smartmon.core.hosts.exception.SshConnectFailedException;
import smartmon.core.hosts.mapper.SmartMonHostMapper;
import smartmon.core.hosts.types.HostAddCommand;
import smartmon.core.hosts.types.HostConfigCommand;
import smartmon.core.hosts.types.SmartMonHost;
import smartmon.core.misc.SshService;
import smartmon.utilities.general.SmartMonException;

@Service
public class HostsServiceImpl implements HostsService {
  @Autowired
  private SmartMonHostMapper smartMonHostMapper;
  @Autowired
  private SshService sshService;

  @Override
  public List<SmartMonHost> getAll() {
    return smartMonHostMapper.selectAll();
  }

  @Override
  public SmartMonHost getHostById(String hostUuid) {
    return smartMonHostMapper.selectByPrimaryKey(hostUuid);
  }

  @Override
  public SmartMonHost addHost(HostAddCommand command) {
    checkSshConnect(command.getManageIp(), command.getSysUsername(), command.getSysPassword(), command.getSshPort());
    SmartMonHost smartMonHostSaved = getSmartMonHostSaved(command);
    SmartMonHost smartMonHost = smartMonHostSaved == null ? new SmartMonHost() : smartMonHostSaved;
    smartMonHost.setManageIp(command.getManageIp());
    smartMonHost.setSysUsername(command.getSysUsername());
    smartMonHost.setSysPassword(command.getSysPassword());
    smartMonHost.setSshPort(command.getSshPort());
    smartMonHost.setIpmiAddress(command.getIpmiAddress());
    smartMonHost.setIpmiUsername(command.getIpmiUsername());
    smartMonHost.setIpmiPassword(command.getIpmiPassword());
    saveSmartMonHost(smartMonHost);
    return smartMonHost;
  }

  private SmartMonHost getSmartMonHostSaved(HostAddCommand hostAddCommand) {
    List<SmartMonHost> smartMonHosts = smartMonHostMapper.selectAll();
    return smartMonHosts.stream()
      .filter(smartMonHost -> smartMonHost.hasIp(hostAddCommand.getManageIp()))
      .findFirst().orElse(null);
  }

  @Override
  public SmartMonHost configHost(String hostUuid, HostConfigCommand command) {
    checkSshConnect(command.getManageIp(), command.getSysUsername(), command.getSysPassword(), command.getSshPort());
    SmartMonHost smartMonHost = smartMonHostMapper.selectByPrimaryKey(hostUuid);
    if (smartMonHost == null) {
      String message = String.format("host[%s] is not exists", hostUuid);
      throw new SmartMonException(SmartMonErrno.HOST_NOT_FOUND, message);
    }
    if (!smartMonHost.hasIp(command.getManageIp())) {
      String message = String.format("host[%s] and ip[%s] is not match", hostUuid, command.getManageIp());
      throw new SmartMonException(SmartMonErrno.HOST_IP_NOT_MATCH, message);
    }
    if (!Objects.equals(smartMonHost.getIpmiAddress(), command.getIpmiAddress())) {
      smartMonHost.setPowerState(null);
    }
    smartMonHost.setManageIp(command.getManageIp());
    smartMonHost.setSysUsername(command.getSysUsername());
    smartMonHost.setSysPassword(command.getSysPassword());
    smartMonHost.setSshPort(command.getSshPort());
    smartMonHost.setIpmiAddress(command.getIpmiAddress());
    smartMonHost.setIpmiUsername(command.getIpmiUsername());
    smartMonHost.setIpmiPassword(command.getIpmiPassword());
    saveSmartMonHost(smartMonHost);
    return smartMonHost;
  }

  private void checkSshConnect(String address, String username, String password, Integer port) {
    if (!sshService.canConnect(address, username, password, port)) {
      String message = String.format("ssh connect error, host[%s] user[%s] port[%s]", address, username, port);
      throw new SshConnectFailedException(message);
    }
  }

  private void saveSmartMonHost(SmartMonHost host) {
    if (host.getHostUuid() == null) {
      host.setHostUuid(UUID.randomUUID().toString());
      Date date = new Date();
      host.setCreateTime(date);
      host.setUpdateTime(date);
      smartMonHostMapper.insert(host);
    } else {
      host.setUpdateTime(new Date());
      smartMonHostMapper.updateByPrimaryKey(host);
    }
  }

  @Override
  public boolean isServer(String ip) {
    return false; //TODO: implement
  }
}
