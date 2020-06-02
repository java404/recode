package smartmon.core.hosts.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.agent.client.AgentClientService;
import smartmon.core.config.SmartMonErrno;
import smartmon.core.hosts.HostsService;
import smartmon.core.hosts.NetworkInfo;
import smartmon.core.hosts.exception.SshConnectFailedException;
import smartmon.core.hosts.mapper.SmartMonHostMapper;
import smartmon.core.hosts.types.HostAddCommand;
import smartmon.core.hosts.types.HostConfigCommand;
import smartmon.core.hosts.types.MonitorNetInterfaceVo;
import smartmon.core.hosts.types.SmartMonHost;
import smartmon.core.misc.SshService;
import smartmon.utilities.general.SmartMonException;
import smartmon.utilities.misc.JsonConverter;

@Service
public class HostsServiceImpl implements HostsService {
  @Autowired
  private SmartMonHostMapper smartMonHostMapper;
  @Autowired
  private SshService sshService;
  @Autowired
  private AgentClientService agentClientService;

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
  public MonitorNetInterfaceVo getMonitorNetInterfaces(String hostUuid) {
    SmartMonHost host = smartMonHostMapper.selectByPrimaryKey(hostUuid);
    if (host == null) {
      return null;
    }
    MonitorNetInterfaceVo vo = new MonitorNetInterfaceVo();
    vo.setHostUuid(host.getHostUuid());
    vo.setHostname(host.getHostname());
    NetworkInfo networkInfo = host.getNetworkInfo();
    if (networkInfo != null) {
      List<String> interfaces = ignoreLo(networkInfo.getInterfaces());;
      vo.setNetInterfaces(interfaces);
    }
    List<String> monitorInterfaces = getMonitorNetInterfaces(host);
    vo.setMonitorNetInterfaces(monitorInterfaces);
    return vo;
  }

  @Override
  public List<String> getMonitorNetInterfaces(SmartMonHost host) {
    if (host.getMonitorNetInterfaces() != null) {
      return host.getMonitorNetworkInterfaces();
    }
    return agentClientService.getMonitorInterfaces(host.getManageIp());
  }

  @Override
  public void configMonitorNetInterfaces(String hostUuid, List<String> monitorNetInterfaces) {
    SmartMonHost smartMonHost = smartMonHostMapper.selectByPrimaryKey(hostUuid);
    if (smartMonHost == null) {
      String message = String.format("host[%s] is not exists", hostUuid);
      throw new SmartMonException(SmartMonErrno.HOST_NOT_FOUND, message);
    }
    monitorNetInterfaces = ignoreLo(monitorNetInterfaces);
    agentClientService.configMonitorInterfaces(smartMonHost.getManageIp(), monitorNetInterfaces);
    String interfaces = JsonConverter.writeValueAsStringQuietly(monitorNetInterfaces);
    smartMonHostMapper.updateMonitorNetInterfacesById(hostUuid, interfaces);
  }

  private List<String> ignoreLo(List<String> netInterfaces) {
    return ListUtils.emptyIfNull(netInterfaces)
      .stream().filter(net -> !Objects.equals("lo", net))
      .collect(Collectors.toList());
  }

  @Override
  public boolean isServer(String ip) {
    return false; //TODO: implement
  }
}
