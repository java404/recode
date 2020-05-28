package smartmon.smartstor.interfaces.web.representation;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.domain.model.Pool;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.infra.cache.DataCacheManager;
import smartmon.smartstor.interfaces.web.representation.assembler.PoolDtoAssembler;
import smartmon.smartstor.web.dto.PoolDto;
import smartmon.smartstor.web.dto.SimplePoolDto;
import smartmon.smartstor.web.dto.StoragePoolDto;
import smartmon.utilities.misc.BeanConverter;


@Slf4j
@Service
public class PoolsRepresentationService extends BaseRepresentationService {
  @Autowired
  private DataCacheManager dataCacheManager;

  public CachedData<PoolDto> getPools(String serviceIp) {
    CachedData<Pool> cachedData = dataCacheManager.gets(serviceIp, Pool.class);
    if (cachedData == null) {
      return null;
    }
    return PoolDtoAssembler.toDtos(cachedData);
  }

  public List<StoragePoolDto> listAllStoragePools() {
    List<StorageHost> iosHosts = getAllIosServiceHosts();
    List<StoragePoolDto> storagePoolDtos = new ArrayList<>();
    getStoragePool(iosHosts, storagePoolDtos);
    return storagePoolDtos;
  }

  private void getStoragePool(List<StorageHost> iosHosts, List<StoragePoolDto> storagePoolDtos) {
    for (StorageHost host : iosHosts) {
      try {
        StoragePoolDto storagePoolDto = BeanConverter.copy(host, StoragePoolDto.class);
        CachedData<PoolDto> cachedData = getPools(host.getListenIp());
        if (cachedData == null) {
          continue;
        }
        List<PoolDto> data = cachedData.getData();
        storagePoolDto.setPools(BeanConverter.copy(data, SimplePoolDto.class));
        storagePoolDto.setHostType(host.getSysMode().getName());
        storagePoolDto.setExpired(cachedData.isExpired());
        storagePoolDto.setError(cachedData.getError());
        storagePoolDtos.add(storagePoolDto);
      } catch (Exception e) {
        log.warn("Get {} luns failed", host.getListenIp(), e);
      }
    }
  }

  public PoolDto getPoolInfo(String serviceIp, String poolName) {
    CachedData<PoolDto> cachedData = getPools(serviceIp);
    if (cachedData == null) {
      return PoolDto.empty();
    }
    List<PoolDto> data = cachedData.getData();
    PoolDto poolDto = data
      .stream()
      .filter(d -> poolName.equals(d.getPoolName()))
      .findFirst()
      .orElseGet(PoolDto::empty);
    return poolDto;
  }

  public StoragePoolDto listStoragePools(String serviceIp) {
    StorageHost storageHost = storageHostRepository.findByServiceIp(serviceIp);
    List<StoragePoolDto> storagePoolDtos  = new ArrayList<>();
    getStoragePool(Lists.newArrayList(storageHost), storagePoolDtos);
    if (CollectionUtils.isEmpty(storagePoolDtos)) {
      return new StoragePoolDto();
    }
    return storagePoolDtos.get(0);
  }
}
