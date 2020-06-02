package smartmon.smartstor.domain.gateway;

import java.util.List;

import smartmon.smartstor.domain.model.ApiVersion;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.domain.model.Group;
import smartmon.smartstor.domain.model.Lun;
import smartmon.smartstor.domain.model.Pool;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.domain.model.StorageNode;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;

public interface SmartstorApiService {
  ApiVersion getApiVersion(String serviceIp);

  List<StorageNode> getNodes(String serviceIp);

  List<StorageHost> getHosts(String serviceIp);

  StorageNode getNodeInfo(String serviceIp);

  List<Disk> getDisks(String serviceIp);

  Disk getDiskInfo(String serviceIp, String diskName);

  PbDataResponseCode addDisk(String serviceIp, String devName, Integer partitionCount, String diskType);

  PbDataResponseCode delDisk(String serviceIp, String diskName);

  PbDataResponseCode diskRaidLedOnState(String serviceIp, String cesAddr);

  PbDataResponseCode diskRaidLedOffState(String serviceIp, String cesAddr);

  List<Group> getGroups(String serviceIp);

  List<Pool> getPools(String serviceIp);

  List<Lun> getLuns(String serviceIp);

  void addGroup(String serviceIp, String groupName);

  PbDataResponseCode delGroup(String serviceIp, String groupName);

  PbDataResponseCode addNodeToGroup(String serviceIp, String groupName, String nodeName);

  PbDataResponseCode removeNodeFromGroup(String serviceIp, String groupName, String nodeName);

  PbDataResponseCode addLun(String serviceIp, String dataDiskName, Boolean baseDisk,
                            String poolName, Integer size, String groupName);

  PbDataResponseCode lunOffline(String serviceIp, String lunName, Boolean asmStatus);

  PbDataResponseCode lunOnline(String serviceIp, String lunName);

  PbDataResponseCode addPool(String serviceIp, String diskName, Boolean isVariable,
                             Long extent, Long bucket, Long sippet);

  PbDataResponseCode addLunToGroup(String serviceIp, String groupName, String lunName);

  PbDataResponseCode lunActive(String serviceIp, String lunName);

  PbDataResponseCode lunInactive(String serviceIp, String lunName, Boolean asmStatus);

  PbDataResponseCode delLun(String serviceIp, String lunName, Boolean isLvvote, Boolean asmStatus);

  PbDataResponseCode confPoolSize(String serviceIp, String poolName, Long size);

  PbDataResponseCode confDirtythreshold(String serviceIp, String poolName, Integer threshLower, Integer threshUpper);

  PbDataResponseCode confSynclevel(String serviceIp, String poolName, Integer syncLevel);

  PbDataResponseCode confSkipThreshold(String serviceIp, String poolName, Integer skipThreshold);

  PbDataResponseCode delPool(String serviceIp, String poolName);
}
