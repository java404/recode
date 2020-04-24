package smartmon.smartstor.domain.gateway;

import java.util.List;

import smartmon.smartstor.domain.model.StorageHost;

public interface DataSyncService {
  void syncAll();

  List<StorageHost> syncHosts();

  void syncDisks(String serviceIp);
}
