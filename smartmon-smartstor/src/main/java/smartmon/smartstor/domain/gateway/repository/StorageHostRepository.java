package smartmon.smartstor.domain.gateway.repository;

import java.util.List;

import smartmon.smartstor.domain.model.StorageHost;

public interface StorageHostRepository {
  void save(List<StorageHost> hosts);

  void save(StorageHost host);

  void delete(StorageHost host);

  List<StorageHost> getAll();

  List<String> getIosServiceIps();

  StorageHost findByServiceIp(String serviceIp);
}
