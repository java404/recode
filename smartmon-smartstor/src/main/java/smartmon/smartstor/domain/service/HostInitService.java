package smartmon.smartstor.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.domain.exception.HostInitException;
import smartmon.smartstor.domain.exception.HostVerifyException;
import smartmon.smartstor.domain.gateway.DataSyncService;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.domain.model.ApiVersion;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.domain.model.StorageNode;

@Slf4j
@Service
public class HostInitService {
  @Autowired
  private SmartstorApiService smartstorApiService;
  @Autowired
  private StorageHostRepository storageHostRepository;
  @Autowired
  private DataSyncService dataSyncService;

  public void initHosts(List<StorageHost> hosts) {
    if (CollectionUtils.isEmpty(hosts)) {
      throw new HostInitException("No available host");
    }
    Map<String, StorageHost> hostMaps = new HashMap<>();
    List<StorageHost> smartstorHosts = new ArrayList<>();
    hosts.forEach(h -> {
      if (CollectionUtils.isEmpty(smartstorHosts)) {
        try {
          smartstorHosts.addAll(smartstorApiService.getHosts(h.getListenIp()));
        } catch (Exception e) {
          //
        }
      }
      hostMaps.put(h.getHostId(), h);
    });
    if (CollectionUtils.isEmpty(smartstorHosts)) {
      throw new HostInitException("Get smartstor hosts failed");
    }
    List<StorageHost> toBeSavedHosts = new ArrayList<>();
    smartstorHosts.forEach(h -> {
      StorageHost storageHost = hostMaps.get(h.getHostId());
      if (storageHost == null) {
        return;
      }
      if (storageHostRepository.findByGuid(storageHost.getGuid()) != null) {
        throw new HostInitException("Exists same host");
      }
      h.setUuid(UUID.randomUUID().toString());
      h.setGuid(storageHost.getGuid());
      h.setListenIp(storageHost.getListenIp());
      toBeSavedHosts.add(h);
    });
    storageHostRepository.save(toBeSavedHosts);
    dataSyncService.syncHosts();
  }

  public void verifyHost(String serviceIp, String listenIp, String hostId) {
    if (serviceIp.equals(listenIp)) {
      return;
    }
    ApiVersion apiVersion = smartstorApiService.getApiVersion(serviceIp);
    if (apiVersion == null) {
      throw new HostVerifyException(String.format("Verify host:[%s] failed: can not get api version", serviceIp));
    }
    if (apiVersion.hostIdEnabled()) {
      StorageNode storageNode = smartstorApiService.getNodeInfo(serviceIp);
      if (storageNode == null) {
        throw new HostVerifyException(String.format("Verify host:[%s] failed: can not get node info", serviceIp));
      }
      if (!Objects.equals(storageNode.getHostId(), hostId)) {
        throw new HostVerifyException(String.format("Verify host:[%s] failed: host id not match", serviceIp));
      }
    }
  }
}
