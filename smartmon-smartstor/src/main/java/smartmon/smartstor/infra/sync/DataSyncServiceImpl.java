package smartmon.smartstor.infra.sync;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.cache.SmartMonCacheService;
import smartmon.smartstor.domain.gateway.DataSyncService;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.domain.model.ApiVersion;
import smartmon.smartstor.domain.model.StorageHost;

@Slf4j
@Service
public class DataSyncServiceImpl implements DataSyncService {
  @Autowired
  private StorageHostRepository storageHostRepository;
  @Autowired
  private SmartstorApiService smartstorApiService;
  @Autowired
  private SmartMonCacheService smartMonCacheService;

  @Override
  public void syncAll() {
    syncHosts();
  }

  @Override
  public List<StorageHost> syncHosts() {
    List<StorageHost> storageHosts = storageHostRepository.getAll();
    Map<String, StorageHost> hostMap = storageHosts.stream()
      .collect(Collectors.toMap(StorageHost::getHostKey, Function.identity(), (oldValue, newValue) -> newValue));
    if (MapUtils.isEmpty(hostMap)) {
      return Collections.emptyList();
    }
    Set<String> hostSyncedKeys = Sets.newHashSet();
    syncHosts(hostMap, hostSyncedKeys);
    deleteInvalidHosts(hostMap, hostSyncedKeys);
    return storageHostRepository.getAll().stream()
      .filter(host -> hostSyncedKeys.contains(host.getHostKey()))
      .collect(Collectors.toList());
  }

  private void syncHosts(Map<String, StorageHost> hostMap, Set<String> hostSyncedKeys) {
    for (Map.Entry<String, StorageHost> entry : hostMap.entrySet()) {
      String hostKey = entry.getKey();
      if (hostSyncedKeys.contains(hostKey)) {
        continue;
      }
      StorageHost storageHost = entry.getValue();
      if (!storageHost.isIos()) {
        continue;
      }
      if (!storageHost.isHostConfigured()) {
        continue;
      }
      String serviceIp = storageHost.getListenIp();
      List<StorageHost> hosts = null;
      try {
        hosts = smartstorApiService.getHosts(serviceIp);
      } catch (Exception e) {
        log.warn(String.format("Get hosts failed by IP:[%s] when sync nodes", serviceIp), e);
      }
      if (CollectionUtils.isEmpty(hosts)) {
        continue;
      }
      String clusterId = getClusterId(storageHost, hostMap, hosts);
      for (StorageHost host : hosts) {
        if (!hostSyncedKeys.add(host.getHostKey())) {
          continue;
        }
        StorageHost hostSync = hostMap.get(host.getHostKey());
        if (hostSync == null) {
          hostSync = new StorageHost();
          hostSync.setUuid(UUID.randomUUID().toString());
          hostSync.setHostId(host.getHostId());
          hostSync.setListenIp(StringUtils.isNotEmpty(host.getListenIp()) ? host.getListenIp() : host.getHostId());
          StorageHost hostHasVersion = findStorageHostVersionNotNull(hosts);
          if (hostHasVersion != null) {
            storageHost.setVersion(hostHasVersion.getVersion());
            storageHost.setVersionNum(hostHasVersion.getVersionNum());
          }
        }
        hostSync.setHostname(host.getHostname());
        hostSync.setListenPort(host.getListenPort());
        hostSync.setBroadcastIp(host.getBroadcastIp());
        hostSync.setSysMode(host.getSysMode());
        hostSync.setTransMode(host.getTransMode());
        hostSync.setNodeIndex(host.getNodeIndex());
        hostSync.setNodeName(host.getNodeName());
        hostSync.setNodeStatus(host.getNodeStatus());
        hostSync.setClusterId(clusterId);
        hostSync.setClusterName(host.getClusterName());
        syncVersion(hostSync);
        storageHostRepository.save(hostSync);
      }
    }
  }

  private String getClusterId(StorageHost storageHost, Map<String, StorageHost> hostMap, List<StorageHost> hosts) {
    String clusterId = storageHost.getClusterId();
    if (StringUtils.isNotEmpty(clusterId)) {
      return clusterId;
    }
    for (StorageHost host : hosts) {
      StorageHost hostInMap = hostMap.get(host.getHostKey());
      if (hostInMap != null && StringUtils.isNotEmpty(clusterId = hostInMap.getClusterId())) {
        break;
      }
    }
    return StringUtils.isNotEmpty(clusterId) ? clusterId : UUID.randomUUID().toString();
  }

  private StorageHost findStorageHostVersionNotNull(List<StorageHost> hosts) {
    StorageHost result = null;
    for (StorageHost host : hosts) {
      if (StringUtils.isNotEmpty(host.getVersion())) {
        result = host;
        break;
      }
    }
    return result;
  }

  private void syncVersion(StorageHost host) {
    try {
      ApiVersion apiVersion = smartstorApiService.getApiVersion(host.getListenIp());
      if (apiVersion != null) {
        host.setVersion(apiVersion.getVersion());
        host.setVersionNum(apiVersion.getVersionValue());
      }
    } catch (Exception e) {
      log.warn(String.format("Get api version failed by IP:[%s] when sync version", host.getListenIp()), e);
    }
  }

  private void deleteInvalidHosts(Map<String, StorageHost> hostMap, Set<String> hostSyncedKeys) {
    if (CollectionUtils.isEmpty(hostSyncedKeys)) {
      return;
    }
    Iterator<Map.Entry<String, StorageHost>> iterator = hostMap.entrySet().iterator();
    while (iterator.hasNext()) {
      StorageHost host = iterator.next().getValue();
      if (!hostSyncedKeys.contains(host.getHostKey())) {
        storageHostRepository.delete(host);
        iterator.remove();
      }
    }
  }

  @Override
  public void syncDisks(String serviceIp) {
  }
}
