package smartmon.smartstor.interfaces.web.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.domain.model.Lun;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.infra.cache.DataCacheManager;
import smartmon.smartstor.interfaces.web.representation.assembler.LunDtoAssembler;
import smartmon.smartstor.web.dto.LunDto;
import smartmon.smartstor.web.dto.SimpleLunDto;
import smartmon.smartstor.web.dto.StorageLunDto;
import smartmon.utilities.misc.BeanConverter;

@Slf4j
@Service
public class LunsRepresentationService extends BaseRepresentationService {
  @Autowired
  private DataCacheManager dataCacheManager;
  @Autowired
  private StorageHostRepository storageHostRepository;

  public CachedData<SimpleLunDto> getSimpleLuns(String serviceIp) {
    CachedData<Lun> cachedData = dataCacheManager.gets(serviceIp, Lun.class);
    if (cachedData == null) {
      return null;
    }
    return LunDtoAssembler.toSimpleDtos(cachedData);
  }

  public List<StorageLunDto> listAllStorageLuns() {
    List<StorageHost> iosHosts = getAllIosServiceHosts();
    List<StorageLunDto> storageLunDtos = new ArrayList<>();
    for (StorageHost host : iosHosts) {
      try {
        StorageLunDto storageLunDto = BeanConverter.copy(host, StorageLunDto.class);
        CachedData<SimpleLunDto> cachedData = getSimpleLuns(host.getListenIp());
        if (cachedData == null) {
          continue;
        }
        List<SimpleLunDto> data = cachedData.getData();
        storageLunDto.setLuns(BeanConverter.copy(data, SimpleLunDto.class));
        storageLunDto.setHostType(host.getSysMode().getName());
        storageLunDto.setExpired(cachedData.isExpired());
        storageLunDto.setError(cachedData.getError());
        storageLunDtos.add(storageLunDto);
      } catch (Exception e) {
        log.warn("Get {} luns failed", host.getListenIp(), e);
      }
    }
    return storageLunDtos;
  }

  public CachedData<SimpleLunDto> getNotInGroupLuns(String serviceIp, String groupName) {
    CachedData<SimpleLunDto> cachedData = getSimpleLuns(serviceIp);
    if (cachedData == null) {
      return null;
    }
    List<SimpleLunDto> data = cachedData.getData();
    data = data
      .stream()
      .filter(d -> !groupName.equals(d.getGroupName()))
      .collect(Collectors.toList());
    return new CachedData<>(data, cachedData.isExpired(), cachedData.getError());
  }

  public LunDto getLunInfo(String listenIp, String lunName) {
    CachedData<Lun> cachedData = dataCacheManager.gets(listenIp, Lun.class);
    if (cachedData == null) {
      return LunDto.empty();
    }
    List<Lun> data = cachedData.getData();
    Lun lun = data
      .stream()
      .filter(d -> {
        String nodeLunName = d.getExtNodeName() == null ? d.getLunName() : d.getExtNodeName() + "_" + d.getLunName();
        return lunName.equals(d.getLunName()) || lunName.equalsIgnoreCase(nodeLunName);
      })
      .findFirst()
      .orElse(null);
    return LunDtoAssembler.toDto(lun);
  }
}
