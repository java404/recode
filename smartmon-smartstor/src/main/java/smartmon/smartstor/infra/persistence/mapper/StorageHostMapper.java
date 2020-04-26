package smartmon.smartstor.infra.persistence.mapper;

import java.util.List;
import smartmon.smartstor.infra.persistence.entity.StorageHostEntity;

public interface StorageHostMapper {
  StorageHostEntity findByUuid(String uuid);

  List<StorageHostEntity> findAll();

  void save(StorageHostEntity hostEntity);

  void update(StorageHostEntity hostEntity);

  void delete(String uuid);

  StorageHostEntity findByServiceIp(String serviceIp);
}
