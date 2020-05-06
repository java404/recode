package smartmon.smartstor.infra.sync;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import smartmon.smartstor.domain.gateway.DataSyncService;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.domain.model.ApiVersion;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.domain.model.Group;
import smartmon.smartstor.domain.model.Lun;
import smartmon.smartstor.domain.model.Pool;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.infra.cache.DataCacheManager;

@Slf4j
@Service
public class DataSyncServiceImpl implements DataSyncService {
  private static ExecutorService executorService = Executors.newCachedThreadPool();

  @Autowired
  private StorageHostRepository storageHostRepository;
  @Autowired
  private SmartstorApiService smartstorApiService;
  @Autowired
  private DataCacheManager dataCacheManager;

  @Override
  public void syncAll() {
    StopWatch stopWatch = new StopWatch("Data sync");
    try {
      stopWatch.start("Sync hosts");
      List<StorageHost> storageHosts = syncHosts();
      stopWatch.stop();
      stopWatch.start("Sync others");
      syncOthers(storageHosts);
      stopWatch.stop();
    } finally {
      log.info(stopWatch.prettyPrint());
    }
  }

  @Override
  public List<StorageHost> syncHosts() {
    try {
      log.info("Sync hosts");
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
    } catch (Exception e) {
      log.error("Sync host failed:", e);
    }
    return Collections.emptyList();
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
      if (!isIp(host.getListenIp())) {
        return;
      }
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
      if (!hostSyncedKeys.contains(host.getHostKey()) && !host.isHostConfigured()) {
        storageHostRepository.delete(host);
        iterator.remove();
      }
    }
  }

  private void syncOthers(List<StorageHost> storageHosts) {
    List<String> iosServiceIps = filterIosServiceIps(storageHosts);
    invokeAllTasks(iosServiceIps);
  }

  private List<String> filterIosServiceIps(List<StorageHost> storageHosts) {
    return storageHosts.stream()
      .filter(StorageHost::isIos)
      .filter(host -> isIp(host.getListenIp()))
      .map(StorageHost::getListenIp)
      .collect(Collectors.toList());
  }

  private void invokeAllTasks(List<String> iosServiceIps) {
    List<Future<?>> futures = Lists.newArrayList();
    futures.add(executorService.submit(() -> syncDisks(iosServiceIps)));
    futures.add(executorService.submit(() -> syncPools(iosServiceIps)));
    futures.add(executorService.submit(() -> syncLuns(iosServiceIps)));
    futures.add(executorService.submit(() -> syncGroups(iosServiceIps)));
    waitComplete(futures);
  }

  private void waitComplete(List<Future<?>> futures) {
    for (Future<?> future : futures) {
      try {
        future.get();
      } catch (Exception e) {
        log.warn("Wait task complete error:", e);
      }
    }
  }

  @Override
  public void syncDisks(List<String> serviceIps) {
    for (String serviceIp : serviceIps) {
      syncDisks(serviceIp);
    }
  }

  @Override
  public void syncDisks(String serviceIp) {
    try {
      log.info("Sync disks of host [{}]", serviceIp);
      List<Disk> disks = smartstorApiService.getDisks(serviceIp);
      dataCacheManager.save(serviceIp, disks, Disk.class);
    } catch (Exception e) {
      log.error(String.format("Sync disks of host [%s] failed:", serviceIp), e);
      dataCacheManager.saveError(serviceIp, e.getMessage(), Disk.class);
    }
  }

  @Override
  public void syncPools(List<String> serviceIps) {
    for (String serviceIp : serviceIps) {
      syncPools(serviceIp);
    }
  }

  @Override
  public void syncPools(String serviceIp) {
    try {
      log.info("Sync pools of host [{}]", serviceIp);
      List<Pool> pools = smartstorApiService.getPools(serviceIp);
      dataCacheManager.save(serviceIp, pools, Pool.class);
    } catch (Exception e) {
      log.error(String.format("Sync pools of host [%s] failed:", serviceIp), e);
      dataCacheManager.saveError(serviceIp, e.getMessage(), Pool.class);
    }
  }

  @Override
  public void syncLuns(List<String> serviceIps) {
    for (String serviceIp : serviceIps) {
      syncLuns(serviceIp);
    }
  }

  @Override
  public void syncLuns(String serviceIp) {
    try {
      log.info("Sync luns of host [{}]", serviceIp);
      List<Lun> luns = smartstorApiService.getLuns(serviceIp);
      dataCacheManager.save(serviceIp, luns, Lun.class);
    } catch (Exception e) {
      log.error(String.format("Sync luns of host [%s] failed:", serviceIp), e);
      dataCacheManager.saveError(serviceIp, e.getMessage(), Lun.class);
    }
  }

  @Override
  public void syncGroups(List<String> serviceIps) {
    for (String serviceIp : serviceIps) {
      syncGroups(serviceIp);
    }
  }

  @Override
  public void syncGroups(String serviceIp) {
    try {
      log.info("Sync groups of host [{}]", serviceIp);
      List<Group> groups = smartstorApiService.getGroups(serviceIp);
      dataCacheManager.save(serviceIp, groups, Group.class);
    } catch (Exception e) {
      log.error(String.format("Sync groups of host [%s] failed:", serviceIp), e);
      dataCacheManager.saveError(serviceIp, e.getMessage(), Group.class);
    }
  }

  private boolean isIp(String ip) {
    if (StringUtils.isBlank(ip)) {
      return false;
    }
    String reg = "^$|^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)($|(?!\\.$)\\.)){4}$";
    return Pattern.matches(reg, ip);
  }
}
