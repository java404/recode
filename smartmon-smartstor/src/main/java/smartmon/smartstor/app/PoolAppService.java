package smartmon.smartstor.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.app.command.PoolAddCommand;
import smartmon.smartstor.app.command.PoolDeleteCommand;
import smartmon.smartstor.app.command.PoolDirtyThresholdCommand;
import smartmon.smartstor.app.command.PoolSizeCommand;
import smartmon.smartstor.app.command.PoolSkipThresholdCommand;
import smartmon.smartstor.app.command.PoolSyncLevelCommand;
import smartmon.smartstor.domain.exception.PbdataPoolException;
import smartmon.smartstor.domain.exception.StorageHostNotFoundException;
import smartmon.smartstor.domain.gateway.DataSyncService;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;

@Slf4j
@Service
public class PoolAppService {
  @Autowired
  private SmartstorApiService smartstorApiService;
  @Autowired
  private DataSyncService dataSyncService;
  @Autowired
  private StorageHostRepository storageHostRepository;

  public void addPool(PoolAddCommand cm) {
    log.info("Add pool: {}", cm.getDiskName());
    exsitStorageHost(cm.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.addPool(cm.getServiceIp(), cm.getDiskName(), null, null, null, null);
    chkResponseFailed(pbDataResponseCode, "Add pool failed");
    dataSyncService.syncPools(cm.getServiceIp());
  }


  private void exsitStorageHost(String serviceIp) {
    if (storageHostRepository.findByServiceIp(serviceIp) == null) {
      throw new StorageHostNotFoundException();
    }
  }

  private void chkResponseFailed(PbDataResponseCode pbDataResponseCode, String errMsgPrefix) {
    if (pbDataResponseCode == null) {
      log.warn(errMsgPrefix + ": No response data");
      throw new PbdataPoolException("No response data");
    }
    if (!pbDataResponseCode.isOk()) {
      String msg = errMsgPrefix.concat(": ").concat(pbDataResponseCode.getMessage());
      log.warn("Exec failed:" + msg);
      throw new PbdataPoolException(msg);
    }
  }

  public void confPoolSize(PoolSizeCommand command) {
    log.info("Config pool size: {}", command.getPoolName());
    exsitStorageHost(command.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.confPoolSize(command.getServiceIp(), command.getPoolName(), command.getSize());
    chkResponseFailed(pbDataResponseCode, "Config pool size failed");
    dataSyncService.syncPools(command.getServiceIp());
  }

  public void confDirtythreshold(PoolDirtyThresholdCommand command) {
    log.info("Config pool dirtythresh: {}", command.getPoolName());
    exsitStorageHost(command.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.confDirtythreshold(command.getServiceIp(), command.getPoolName(), command.getThreshLower(), command.getThreshUpper());
    chkResponseFailed(pbDataResponseCode, "Config pool dirtythresh failed");
    dataSyncService.syncPools(command.getServiceIp());
  }

  public void confSyncLevel(PoolSyncLevelCommand command) {
    log.info("Config pool synclevel: {}", command.getPoolName());
    exsitStorageHost(command.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.confSynclevel(command.getServiceIp(), command.getPoolName(), command.getSyncLevel());
    chkResponseFailed(pbDataResponseCode, "Config pool synclevel failed");
    dataSyncService.syncPools(command.getServiceIp());
  }

  public void confSkipThreshold(PoolSkipThresholdCommand command) {
    log.info("Config pool skip threshold: {}", command.getPoolName());
    exsitStorageHost(command.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.confSkipThreshold(command.getServiceIp(), command.getPoolName(), command.getSkipThreshold());
    chkResponseFailed(pbDataResponseCode, "Config pool skip threshold failed");
    dataSyncService.syncPools(command.getServiceIp());
  }

  public void deletePool(PoolDeleteCommand command) {
    log.info("Delete pool : {}", command.getPoolName());
    exsitStorageHost(command.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.delPool(command.getServiceIp(), command.getPoolName());
    chkResponseFailed(pbDataResponseCode, "Delete pool failed");
    dataSyncService.syncPools(command.getServiceIp());
  }
}
