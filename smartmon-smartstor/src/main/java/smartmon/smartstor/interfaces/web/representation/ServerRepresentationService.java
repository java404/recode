package smartmon.smartstor.interfaces.web.representation;

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.web.dto.HostInitInfoDto;

@Service
public class ServerRepresentationService {
  @Autowired
  private StorageHostRepository storageHostRepository;
  @Autowired
  private SmartstorApiService smartstorApiService;

  public HostInitInfoDto getInitInfo() {
    HostInitInfoDto initInfoDto = new HostInitInfoDto();
    List<StorageHost> storageHosts = storageHostRepository.getAll();
    if (CollectionUtils.isEmpty(storageHosts)) {
      return initInfoDto;
    }
    initInfoDto.setInit(true);
    return initInfoDto;
  }
}
