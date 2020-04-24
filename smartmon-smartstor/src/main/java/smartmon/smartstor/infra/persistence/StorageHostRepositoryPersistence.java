package smartmon.smartstor.infra.persistence;

import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.infra.persistence.entity.StorageHostEntity;
import smartmon.smartstor.infra.persistence.mapper.StorageHostMapper;

@Component
public class StorageHostRepositoryPersistence implements StorageHostRepository {
  @Autowired
  private StorageHostMapper hostMapper;

  @Override
  @Transactional
  public void save(List<StorageHost> hosts) {
    List<String> guids = getGuids();
    hosts.forEach(host -> saveOrUpdate(guids, host));
  }

  @Override
  @Transactional
  public void save(StorageHost host) {
    saveOrUpdate(getGuids(), host);
  }

  private List<String> getGuids() {
    List<StorageHostEntity> allHostEntities = hostMapper.findAll();
    return allHostEntities
      .stream()
      .map(StorageHostEntity::getGuid)
      .collect(Collectors.toList());
  }

  private void saveOrUpdate(List<String> guids, StorageHost host) {
    StorageHostEntity entity = new StorageHostEntity();
    BeanUtils.copyProperties(host, entity, "uuid");
    if (guids.contains(host.getGuid())) {
      entity.setUpdateTime(LocalDateTime.now());
      hostMapper.updateByGuid(entity);
    } else {
      hostMapper.save(entity);
    }
  }

  @Override
  public void delete(StorageHost host) {
    hostMapper.delete(host.getUuid());
  }

  @Override
  public List<StorageHost> getAll() {
    List<StorageHostEntity> hostEntities = hostMapper.findAll();
    if (CollectionUtils.isEmpty(hostEntities)) {
      return Lists.newArrayList();
    }
    List<StorageHost> storageHosts = new ArrayList<>();
    hostEntities.forEach(entity -> {
      StorageHost host = new StorageHost();
      BeanUtils.copyProperties(entity, host);
      storageHosts.add(host);
    });
    return storageHosts;
  }

  @Override
  public List<String> getIosServiceIps() {
    return getAll().stream()
      .filter(StorageHost::isIos)
      .map(StorageHost::getListenIp)
      .collect(Collectors.toList());
  }

  @Override
  public StorageHost findByServiceIp(String serviceIp) {
    StorageHostEntity entity = hostMapper.findByServiceIp(serviceIp);
    StorageHost host = new StorageHost();
    BeanUtils.copyProperties(entity, host);
    return host;
  }
}
