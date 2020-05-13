package smartmon.falcon.host;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.client.FalconClientService;
import smartmon.falcon.remote.config.FalconApiComponent;
import smartmon.falcon.remote.types.host.FalconHost;
import smartmon.falcon.remote.types.host.FalconHostGroup;
import smartmon.falcon.remote.types.host.FalconHostGroupCreateParam;
import smartmon.falcon.remote.types.host.FalconHostGroupQueryParam;
import smartmon.falcon.remote.types.host.FalconHostGroupUpdateParam;
import smartmon.falcon.remote.types.host.FalconHosts;
import smartmon.utilities.misc.BeanConverter;

@Service
public class HostGroupServiceImpl implements HostGroupService {
  @Autowired
  private FalconClientService falconClientService;
  @Autowired
  private FalconApiComponent falconApiComponent;


  @Override
  public List<HostGroup> getHostGroups() {
    return getHostGroups(null);
  }

  @Override
  public List<HostGroup> getHostGroups(String groupRegex) {
    final FalconClient client = falconApiComponent.getFalconClient();
    if (StringUtils.isEmpty(groupRegex)) {
      groupRegex = ".";
    }
    // TODO: call GET method api/v1/hostgroup?q={groupRegex}
    final List<FalconHostGroup> falconHostGroups = client.listHostGroupsByGroupRegex(
      new FalconHostGroupQueryParam(groupRegex), falconApiComponent.getApiToken());
    return ListUtils.emptyIfNull(BeanConverter.copy(falconHostGroups, HostGroup.class));
  }

  @Override
  public HostGroup createHostGroup(String name, String note) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    // TODO: call POST method api/v1/hostgroup
    final FalconHostGroup hostGroup = falconClient.createHostGroup(
      new FalconHostGroupCreateParam(name, note), falconApiComponent.getApiToken());
    return BeanConverter.copy(hostGroup, HostGroup.class);
  }

  @Override
  public void updateHostGroup(Integer id, String name, String note) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    // TODO: call PUT method api/v1/hostgroup
    falconClient.updateHostGroup(new FalconHostGroupUpdateParam(id, name, note), falconApiComponent.getApiToken());
  }

  @Override
  public void deleteHostGroup(Integer id) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    // TODO: call DELETE method api/v1/hostgroup/{id}
    falconClient.delHostGroup(id, falconApiComponent.getApiToken());
  }

  @Override
  public List<Host> getHostsByGroupId(Integer groupId) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    // TODO: call GET method api/v1/hostgroup/{groupId}, and parse the values of key 'hosts'
    final FalconHosts hosts = falconClient.getHostsByGroupId(groupId, falconApiComponent.getApiToken());
    List<FalconHost> hostList = new ArrayList<>();
    if (hosts != null) {
      hostList = hosts.getHostList();
    }
    return ListUtils.emptyIfNull(BeanConverter.copy(hostList, Host.class));
  }

  @Override
  public void addHostsToHostGroup(Integer groupId, Set<String> hosts) {
    // TODO: call PATCH method api/v1/hostgroup/{groupId}/host
  }

  @Override
  public void removeHostsFromHostGroup(Integer groupId, Set<String> hosts) {
    // TODO: call PATCH method api/v1/hostgroup/{groupId}/host
  }
}
