package smartmon.smartstor.infra.remote.client;

import feign.Client;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import smartmon.smartstor.infra.remote.requests.PbDataRequestDiskManager;
import smartmon.smartstor.infra.remote.requests.PbDataRequestGroupManager;
import smartmon.smartstor.infra.remote.requests.PbDataRequestLunManager;
import smartmon.smartstor.infra.remote.requests.PbDataRequestNodeManager;
import smartmon.smartstor.infra.remote.requests.PbDataRequestPoolManager;
import smartmon.smartstor.infra.remote.requests.PbDataRequestVersion;
import smartmon.smartstor.infra.remote.types.PbDataApiVersion;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskAddParam;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskInfo;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskInfoV30;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskInfos;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskInfosV30;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskV30;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupAddNodeParam;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupAddParam;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupResponse;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupConfigParam;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupInfos;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunConfigParam;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunCreateParam;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunDelParam;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunInfos;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunResponse;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunStateParam;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeConfigParam;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeInfo;
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
import smartmon.utilities.remote.RemoteApiBuilder;

@Slf4j
public class PbDataClient {
  @Getter
  private final TargetHost targetHost;
  private final PbDataApiVersion apiVersionInfo;
  private final PbDataRequestNodeManager nodeManagerRequest;
  private final PbDataRequestDiskManager diskManagerRequest;
  private final PbDataRequestGroupManager groupManagerRequest;
  private final PbDataRequestLunManager lunManagerRequest;
  private final PbDataRequestPoolManager poolManagerRequest;

  public PbDataClient(TargetHost targetHost) {
    this(targetHost, null);
  }

  /** init pbdata client. */
  public PbDataClient(TargetHost targetHost, Client client) {
    this.targetHost = targetHost;

    // Query API version
    this.apiVersionInfo = queryApiVersion();

    // PbData API Requests
    this.nodeManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new PbDataResponseDecoder())
      .withApiPrefix("/api/" + apiVersionInfo.getVersion())
      .build(PbDataRequestNodeManager.class);
    this.diskManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new PbDataResponseDecoder())
      .withApiPrefix("/api/" + apiVersionInfo.getVersion())
      .build(PbDataRequestDiskManager.class);
    this.groupManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new PbDataResponseDecoder())
      .withApiPrefix("/api/" + apiVersionInfo.getVersion())
      .build(PbDataRequestGroupManager.class);
    this.lunManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new PbDataResponseDecoder())
      .withApiPrefix("/api/" + apiVersionInfo.getVersion())
      .build(PbDataRequestLunManager.class);
    this.poolManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new PbDataResponseDecoder())
      .withApiPrefix("/api/" + apiVersionInfo.getVersion())
      .build(PbDataRequestPoolManager.class);
  }

  private PbDataApiVersion queryApiVersion() {
    return new RemoteApiBuilder(this.targetHost)
      .withClient(null).build(PbDataRequestVersion.class).getVersion();
  }

  /** get api version. */
  public PbDataApiVersion getApiVersionInfo() {
    if (this.apiVersionInfo == null) {
      queryApiVersion();
    }
    return this.apiVersionInfo;
  }

  public PbDataNodeInfos listNodes() {
    return this.nodeManagerRequest.listNodes();
  }

  public PbDataNodeItem getNodeInfo() {
    final PbDataNodeInfo nodeInfo = this.nodeManagerRequest.getNodeInfo();
    return nodeInfo == null ? null : nodeInfo.getNodeInfo();
  }

  public void nodeConfig(PbDataNodeConfigParam param) {
    this.nodeManagerRequest.nodeConfig(param);
  }

  /** pbdata list disks. */
  public PbDataDiskInfos listDisks() {
    if (apiVersionInfo.isApiVer30()) {
      return PbDataDiskInfosV30.adjust(diskManagerRequest.listDisksV30());
    }
    return diskManagerRequest.listDisks();
  }

  /** pbdata get diskinfo. */
  public PbDataDiskInfo getDiskInfo(String diskName) {
    if (apiVersionInfo.isApiVer30()) {
      final PbDataDiskV30 diskInfoV30 = diskManagerRequest.getDiskInfoV30(diskName);
      if (diskInfoV30 != null) {
        return PbDataDiskInfoV30.adjust(diskInfoV30.getDiskInfo());
      }
      return null;
    }
    return diskManagerRequest.getDiskInfo(diskName).getDiskInfo();
  }

  public PbDataResponseCode diskAdd(PbDataDiskAddParam param) {
    return this.diskManagerRequest.diskAdd(param);
  }

  public PbDataResponseCode diskDel(String diskName) {
    return this.diskManagerRequest.diskDel(diskName);
  }

  public PbDataResponseCode diskRaidLedOnState(String cesAddr) {
    return this.diskManagerRequest.diskRaidLedOnState(cesAddr);
  }

  public PbDataResponseCode diskRaidLedOffState(String cesAddr) {
    return this.diskManagerRequest.diskRaidLedOffState(cesAddr);
  }

  public PbDataGroupInfos listGroups() {
    return this.groupManagerRequest.listGroups();
  }

  public PbDataGroupResponse getGroupInfoByName(String groupName) {
    return this.groupManagerRequest.getGroupInfoByName(groupName);
  }

  public PbDataGroupResponse getGroupInfoById(String groupId) {
    return this.groupManagerRequest.getGroupInfoById(groupId);
  }

  public PbDataResponseCode groupConfig(PbDataGroupConfigParam groupConfigParam, String groupName) {
    return this.groupManagerRequest.groupConfig(groupConfigParam, groupName);
  }

  public PbDataGroupResponse groupAdd(PbDataGroupAddParam groupAddParam) {
    return this.groupManagerRequest.groupAdd(groupAddParam);
  }

  public PbDataResponseCode groupDel(String groupName) {
    return this.groupManagerRequest.groupDel(groupName);
  }

  public PbDataResponseCode groupAddNode(PbDataGroupAddNodeParam groupAddNodeParam, String groupName) {
    return this.groupManagerRequest.groupAddNode(groupAddNodeParam, groupName);
  }

  public PbDataResponseCode groupDelNode(String groupName, String nodeName) {
    return this.groupManagerRequest.groupDelNode(groupName, nodeName);
  }

  public PbDataLunInfos listluns() {
    return this.lunManagerRequest.listLuns();
  }

  public PbDataLunResponse getLunInfo(String lunName) {
    return this.lunManagerRequest.getLunInfo(lunName);
  }

  public PbDataResponseCode lunCreate(PbDataLunCreateParam lunCreateParam) {
    return this.lunManagerRequest.lunCreate(lunCreateParam);
  }

  public PbDataResponseCode lunDel(String lunName, Boolean asmStatus, Boolean vote) {
    return this.lunManagerRequest.lunDel(lunName, new PbDataLunDelParam(asmStatus, vote));
  }

  public PbDataResponseCode lunOnline(String lunName) {
    return this.lunManagerRequest.lunOnline(lunName);
  }

  public PbDataResponseCode lunOffline(String lunName, Boolean asmStatus) {
    return this.lunManagerRequest.lunOffline(lunName, new PbDataLunStateParam(asmStatus));
  }

  public PbDataResponseCode lunActive(String lunName) {
    return this.lunManagerRequest.lunActive(lunName);
  }

  public PbDataResponseCode lunInActive(String lunName, Boolean asmStatus) {
    return this.lunManagerRequest.lunInActive(lunName, new PbDataLunStateParam(asmStatus));
  }

  public PbDataResponseCode lunConfig(PbDataLunConfigParam lunConfigParam, String lunName) {
    return this.lunManagerRequest.lunConfig(lunConfigParam, lunName);
  }

  public PbDataPoolInfos listPools() {
    return this.poolManagerRequest.listPools();
  }

  public PbDataResponseCode poolCreate(PbDataPoolCreateParam poolCreateParam) {
    return this.poolManagerRequest.poolCreate(poolCreateParam);
  }

  public PbDataResponseCode poolDel(String poolName) {
    return this.poolManagerRequest.poolDel(poolName);
  }

  public PbDataResponseCode poolConfigSizeUpdata(String poolName, PbDataPoolSizeConfigParam sizeConfigParam) {
    return this.poolManagerRequest.poolSizeUpdata(poolName, sizeConfigParam);
  }

  public PbDataResponseCode poolConfigDirtyThresh(String poolName, PbDataPoolDirtyConfigParam dirtyConfigParam) {
    return this.poolManagerRequest.poolConfigDirtyThresh(poolName, dirtyConfigParam);
  }

  public PbDataResponseCode poolConfigSynClevel(String poolName, PbDataPoolSynClevelConfgParam synClevelConfgParam) {
    return this.poolManagerRequest.poolConfigSynClevel(poolName, synClevelConfgParam);
  }

  public PbDataResponseCode poolConfigCacheMode(String poolName, PbDataPoolCacheConfigParam cacheConfigParam) {
    return this.poolManagerRequest.poolConfigCacheModel(poolName, cacheConfigParam);
  }

  public PbDataResponseCode poolConfigSkip(String poolName, PbDataPoolSkipConfigParam skipConfigParam) {
    return this.poolManagerRequest.poolConfigSkip(poolName, skipConfigParam);
  }
}
