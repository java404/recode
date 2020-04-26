package smartmon.smartstor.infra.remote;

import org.junit.Ignore;
import org.junit.Test;
import smartmon.smartstor.infra.remote.client.PbDataClient;
import smartmon.smartstor.infra.remote.types.PbDataApiVersion;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskInfos;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskAddParam;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupAddNodeParam;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupAddParam;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupConfigParam;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupResponse;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupInfos;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunConfigParam;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunCreateParam;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunInfos;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunResponse;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeConfigParam;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeInfos;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeItem;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolCacheConfigParam;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolCreateParam;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolDirtyConfigParam;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolInfos;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolSizeConfigParam;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolSkipConfigParam;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolSynClevelConfgParam;
import smartmon.utilities.misc.TargetHost;

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
    final PbDataResponseCode raidLedOnState = client.diskRaidLedOnState("0:16:9");
    System.out.println(raidLedOnState);
    final PbDataResponseCode ledOffState = client.diskRaidLedOffState("0:16:9");
    System.out.println(ledOffState);
  }

  @Test
  @Ignore
  public void groupListTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.216", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataGroupInfos pbDataGroupInfos = client.listGroups();
    System.out.println(pbDataGroupInfos);
  }

  @Test
  @Ignore
  public void groupOperateTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.216", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataGroupResponse groupResponse = client.groupAdd(new PbDataGroupAddParam("hello", "jjj"));
    System.out.println(groupResponse);
    final PbDataGroupResponse groupInfoByName = client.getGroupInfoByName("hello");
    System.out.println(groupInfoByName);
    final PbDataResponseCode pbDataResponseCode = client.groupDel("hello");
    System.out.println(pbDataResponseCode);
  }

  @Test
  @Ignore
  public void groupConfigTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.216", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataResponseCode pbDataResponseCode = client.groupConfig(new PbDataGroupConfigParam(null, "111"), "group01");
    System.out.println(pbDataResponseCode);
  }

  @Test
  @Ignore
  public void groupNodeTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.216", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataResponseCode groupAddNode = client.groupAddNode(new PbDataGroupAddNodeParam("du098", null), "group02");
    System.out.println(groupAddNode);
    final PbDataResponseCode groupDelNode = client.groupDelNode("group02", "du098");
    System.out.println(groupDelNode);
  }

  @Test
  @Ignore
  public void lunListTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.216", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataLunInfos listluns = client.listluns();
    System.out.println(listluns);
    final PbDataLunResponse lunInfo = client.getLunInfo("su098_lun02");
    System.out.println(lunInfo);
  }

  @Test
  @Ignore
  public void lunOperateTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.216", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataResponseCode lunCreate = client.lunCreate(new PbDataLunCreateParam("hd01p1", null, null, null, null));
    System.out.println(lunCreate);
    final PbDataResponseCode lunDel = client.lunDel("su098_lun01", null, null);
    System.out.println(lunDel);
  }

  @Test
  @Ignore
  public void lunConfigTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.216", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataResponseCode pbDataResponseCode = client.lunConfig(new PbDataLunConfigParam("group02"), "su098_lun01");
    System.out.println(pbDataResponseCode);
  }

  @Test
  @Ignore
  public void lunLineTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.216", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataResponseCode lunOnline = client.lunOnline("su098_lun01");
    System.out.println(lunOnline);
    final PbDataResponseCode lunOffline = client.lunOffline("su098_lun01", null);
    System.out.println(lunOffline);
  }

  @Test
  @Ignore
  public void lunActiveTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.216", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataResponseCode lunActive = client.lunActive("su098_lun01");
    System.out.println(lunActive);
    final PbDataResponseCode lunInActive = client.lunInActive("su098_lun01", null);
    System.out.println(lunInActive);
  }

  @Test
  @Ignore
  public void poolListTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.218", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataPoolInfos pbDataPoolInfos = client.listPools();
    System.out.println(pbDataPoolInfos);
  }

  @Test
  @Ignore
  public void poolOperateTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.218", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataResponseCode poolCreate = client.poolCreate(new PbDataPoolCreateParam("sd03p1", null, null, null, null));
    System.out.println(poolCreate);
    final PbDataResponseCode poolDel = client.poolDel("pool03");
    System.out.println(poolDel);
  }

  @Test
  @Ignore
  public void poolConfigTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.218", 9000).build();
    final PbDataClient client = new PbDataClient(targetHost);
    final PbDataResponseCode poolConfigSizeUpdata = client.poolConfigSizeUpdata("pool01", new PbDataPoolSizeConfigParam(200L));
    System.out.println(poolConfigSizeUpdata);
    final PbDataResponseCode poolConfigCacheMode = client.poolConfigCacheMode("pool01", new PbDataPoolCacheConfigParam("through", null));
    System.out.println(poolConfigCacheMode);
    final PbDataResponseCode poolConfigDirtyThresh = client.poolConfigDirtyThresh("pool01", new PbDataPoolDirtyConfigParam(30, 70));
    System.out.println(poolConfigDirtyThresh);
    final PbDataResponseCode poolConfigSynClevel = client.poolConfigSynClevel("pool01", new PbDataPoolSynClevelConfgParam(5));
    System.out.println(poolConfigSynClevel);
    final PbDataResponseCode poolConfigSkip = client.poolConfigSkip("pool01", new PbDataPoolSkipConfigParam(24));
    System.out.println(poolConfigSkip);
  }
}
