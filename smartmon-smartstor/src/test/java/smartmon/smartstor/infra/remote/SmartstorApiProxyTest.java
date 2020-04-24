package smartmon.smartstor.infra.remote;

import org.junit.Ignore;
import org.junit.Test;
import smartmon.smartstor.infra.remote.client.PbDataClient;
import smartmon.smartstor.infra.remote.types.PbDataApiVersion;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskInfos;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskAddParam;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeConfigParam;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeInfos;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeItem;
import smartmon.utilities.misc.JsonConverter;
import smartmon.utilities.misc.TargetHost;

import java.util.HashMap;
import java.util.Map;

public class SmartstorApiProxyTest {

  @Test
  @Ignore
  public void versionTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.216", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataApiVersion version = client.getApiVersionInfo();
    System.out.println(version);
  }

  @Test
  @Ignore
  public void nodeTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.216", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataApiVersion version = client.getApiVersionInfo();
    System.out.println(version);

    final PbDataNodeInfos nodes = client.listNodes();
    System.out.println(nodes);

    final PbDataNodeItem nodeInfo = client.getNodeInfo();
    System.out.println(nodeInfo);
  }

  @Test
  @Ignore
  public void nodeConfigTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.140", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    PbDataNodeConfigParam param = new PbDataNodeConfigParam();
    param.setClusterName("cluster-001");
    param.setNodeId(1);
    param.setTrsType(1);
    client.nodeConfig(param);
  }

  @Test
  @Ignore
  public void diskGetTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.218", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);

    final PbDataApiVersion version = client.getApiVersionInfo();
    System.out.println(version);

    final PbDataDiskInfos disks = client.listDisks();
    System.out.println(disks);

  }

  @Test
  @Ignore
  public void diskAddTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.140", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);

    PbDataDiskAddParam param = new PbDataDiskAddParam();
    param.setDevName("/dev/sdf");
    param.setDiskType("hdd");
    param.setPartitionCount(2);
    System.out.println(client.diskAdd(param));
  }

  @Test
  @Ignore
  public void diskDelTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.140", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    System.out.println(client.diskDel("hd08"));
  }

  @Test
  @Ignore
  public void diskLedTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.216", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataResponseCode pbDataResponseCode = client.diskRaidLedOnState("0:16:9");
    System.out.println(pbDataResponseCode);
    final PbDataResponseCode pbDataResponseCode1 = client.diskRaidLedOffState("0:16:9");
    System.out.println(pbDataResponseCode1);
  }
}
