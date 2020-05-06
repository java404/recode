package smartmon.falcon.remote;


import org.junit.Ignore;
import org.junit.Test;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.host.FalconHostGroup;
import smartmon.falcon.remote.types.host.FalconHostGroupCreateParam;
import smartmon.falcon.remote.types.host.FalconHostGroupQueryParam;
import smartmon.falcon.remote.types.host.FalconHostGroupUpdateParam;
import smartmon.utilities.misc.TargetHost;

import java.util.List;

public class HostGroupRemoteTest {

  @Ignore
  @Test
  public void hostGroupListTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.132", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final List<FalconHostGroup> falconHostGroups = falconClient.listHostGroups();
    System.out.println(falconHostGroups);
  }

  @Ignore
  @Test
  public void hostGroupListByGroupRegexTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.132", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final List<FalconHostGroup> falconHostGroups = falconClient.listHostGroupsByGroupRegex("phegdata");
    System.out.println(falconHostGroups);
  }

  @Ignore
  @Test
  public void hostGroupAddTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.132", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final FalconHostGroup hostGroup = falconClient.createHostGroup("uz1iff", "777");
    System.out.println(hostGroup);
  }

  @Ignore
  @Test
  public void hostGroupUpdataTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.132", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final FalconResponseData falconResponseData = falconClient.updateHostGroup(34,"sdfhy","666");
    System.out.println(falconResponseData);
  }

  @Ignore
  @Test
  public void hostGroupDelTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.132", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final FalconResponseData falconResponseData = falconClient.delHostGroup(34);
    System.out.println(falconResponseData);
  }
}
