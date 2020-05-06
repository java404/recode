package smartmon.smartstor.domain.gateway;

import java.util.List;

import smartmon.smartstor.domain.model.StorageHost;

public interface DataSyncService {
  void syncAll();

  List<StorageHost> syncHosts();

  void syncDisks(List<String> serviceIps);

  void syncDisks(String serviceIp);

  void syncPools(List<String> serviceIps);

  void syncPools(String serviceIp);

  void syncLuns(List<String> serviceIps);

  void syncLuns(String serviceIp);

  void syncGroups(List<String> serviceIps);

  void syncGroups(String serviceIp);
}
