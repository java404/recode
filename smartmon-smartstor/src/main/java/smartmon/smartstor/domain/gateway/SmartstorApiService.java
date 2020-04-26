package smartmon.smartstor.domain.gateway;

import java.util.List;

import smartmon.smartstor.domain.model.ApiVersion;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.domain.model.StorageNode;

public interface SmartstorApiService {
  ApiVersion getApiVersion(String serviceIp);

  List<StorageNode> getNodes(String serviceIp);

  List<StorageHost> getHosts(String serviceIp);

  StorageNode getNodeInfo(String serviceIp);

  List<Disk> getDisks(String serviceIp);

  Disk getDiskInfo(String serviceIp, String diskName);

  void addDisk(String serviceIp, String devName, Integer partitionCount, String diskType);

  void delDisk(String serviceIp, String diskName);

  void diskRaidLedOnState(String serviceIp, String cesAddr);

  void diskRaidLedOffState(String serviceIp, String cesAddr);
}
