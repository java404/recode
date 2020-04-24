package smartmon.smartstor.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.domain.model.Disk;

@Service
public class SmartstorApiServiceWrapper {
  @Autowired
  private SmartstorApiService smartstorApiService;
  @Autowired
  private StorageHostRepository storageHostRepository;

  public List<Disk> getDisks() {
    List<String> serviceIps = storageHostRepository.getIosServiceIps();
    return smartstorApiService.getDisks(serviceIps);
  }
}
