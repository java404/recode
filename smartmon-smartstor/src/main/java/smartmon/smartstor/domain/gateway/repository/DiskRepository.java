package smartmon.smartstor.domain.gateway.repository;

import java.util.List;

import smartmon.smartstor.domain.model.Disk;

public interface DiskRepository {
  List<Disk> getDisks();
}
