package smartmon.smartstor.interfaces.web.representation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.infra.cache.DataCacheManager;
import smartmon.smartstor.interfaces.web.representation.assembler.DiskDtoAssembler;
import smartmon.smartstor.interfaces.web.representation.dto.DiskDto;

@Service
public class DiskRepresentationService {
  @Autowired
  private DataCacheManager dataCacheManager;

  public CachedData<DiskDto> getDisks(String serviceIp) {
    CachedData<Disk> disks = dataCacheManager.getDisks(serviceIp);

    return DiskDtoAssembler.toDtos(disks);
  }
}
