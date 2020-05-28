package smartmon.smartstor.domain.service;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.app.command.HostInitCommand;
import smartmon.smartstor.domain.exception.HostInitException;
import smartmon.smartstor.domain.gateway.DataSyncService;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.domain.model.StorageNode;
import smartmon.utilities.misc.BeanConverter;

@Slf4j
@Service
public class HostInitService {
  @Autowired
  private SmartstorApiService smartstorApiService;
  @Autowired
  private StorageHostRepository storageHostRepository;
  @Autowired
  private DataSyncService dataSyncService;

  public synchronized void initHost(HostInitCommand command) {
    try {
      StorageHost storageHostSaved = getStorageHostSaved(command.getHostId(), command.getListenIp());
      if (storageHostSaved != null && storageHostSaved.isHostConfigured()) {
        return;
      }
      StorageHost storageHost;
      if (storageHostSaved != null) {
        storageHost = storageHostSaved;
      } else {
        storageHost = getStorageHostRemote(command.getHostId(), command.getListenIp());
        storageHost.setUuid(UUID.randomUUID().toString());
      }
      storageHost.setGuid(command.getGuid());
      storageHost.setListenIp(command.getListenIp());
      storageHostRepository.save(storageHost);
      dataSyncService.syncHosts();
    } catch (HostInitException err) {
      throw err;
    } catch (Exception err) {
      log.error("host init failed", err);
      throw new HostInitException(err.getMessage());
    }
  }

  private StorageHost getStorageHostSaved(String hostId, String listenIp) {
    return storageHostRepository.getAll().stream()
      .filter(storageHost -> storageHost.match(hostId, listenIp)).findFirst()
      .orElse(null);
  }

  private StorageHost getStorageHostRemote(String hostId, String listenIp) {
    StorageNode storageNode = smartstorApiService.getNodeInfo(listenIp);
    if (storageNode == null) {
      throw new HostInitException("host not exists");
    }
    return BeanConverter.copy(storageNode, StorageHost.class);
  }
}
