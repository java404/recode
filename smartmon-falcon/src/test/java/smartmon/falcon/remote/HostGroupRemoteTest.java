package smartmon.falcon.remote;


import org.junit.Ignore;
import org.junit.Test;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.types.FalconApiToken;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.host.FalconHostGroup;
import smartmon.falcon.remote.types.host.FalconHostGroupCreateParam;
import smartmon.falcon.remote.types.host.FalconHostGroupQueryParam;
import smartmon.falcon.remote.types.host.FalconHostGroupUpdateParam;
import smartmon.utilities.misc.JsonConverter;
import smartmon.utilities.misc.TargetHost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HostGroupRemoteTest {

  @Ignore
  @Test
  public void hostGroupListTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.55", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    Map<String, String> map = new HashMap<>();
    final FalconApiToken root = new FalconApiToken("root", "default-token-used-in-server-side");
    map.put("ApiToken", JsonConverter.writeValueAsStringQuietly(root));
    final List<FalconHostGroup> falconHostGroups = falconClient.listHostGroups(map);
    System.out.println(falconHostGroups);
  }

  @Ignore
  @Test
  public void hostGroupListByGroupRegexTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.132", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final List<FalconHostGroup> falconHostGroups = falconClient.listHostGroupsByGroupRegex(new FalconHostGroupQueryParam("phegdata"), null);
    System.out.println(falconHostGroups);
  }

  @Ignore
  @Test
  public void hostGroupAddTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.132", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final FalconHostGroup hostGroup = falconClient.createHostGroup(new FalconHostGroupCreateParam("uz1iff", "777"), null);
    System.out.println(hostGroup);
  }

  @Ignore
  @Test
  public void hostGroupUpdataTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.132", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final FalconResponseData falconResponseData = falconClient.updateHostGroup(new FalconHostGroupUpdateParam(34,"sdfhy","666"), null);
    System.out.println(falconResponseData);
  }

  @Ignore
  @Test
  public void hostGroupDelTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.132", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final FalconResponseData falconResponseData = falconClient.delHostGroup(34, null);
    System.out.println(falconResponseData);
  }
}
