package smartmon.falcon.host;

import java.util.List;
import java.util.Set;

public interface HostGroupService {
  List<HostGroup> getHostGroups();

  List<HostGroup> getHostGroups(String groupRegex);

  HostGroup createHostGroup(String name, String note);

  void updateHostGroup(Integer id, String name, String note);

  void deleteHostGroup(Integer id);

  List<Host> getHostsByGroupId(Integer groupId);

  void addHostsToHostGroup(Integer groupId, Set<String> hosts);

  void removeHostsFromHostGroup(Integer groupId, Set<String> hosts);
}
