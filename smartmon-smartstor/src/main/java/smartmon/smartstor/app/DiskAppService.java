package smartmon.smartstor.app;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.app.command.DiskAddCommand;
import smartmon.smartstor.app.command.DiskDelCommand;
import smartmon.smartstor.app.command.DiskRaidLedCommand;
import smartmon.smartstor.domain.exception.PbdataDiskException;
import smartmon.smartstor.domain.exception.StorageHostNotFoundException;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.domain.model.DiskRaidLedInfo;
import smartmon.smartstor.domain.model.enums.DiskRaidLedOperateEnum;
import smartmon.smartstor.domain.model.enums.DiskRaidLedStateEnum;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.infra.cache.DataCacheManager;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.smartstor.infra.sync.DataSyncServiceImpl;

@Slf4j
@Service
public class DiskAppService {
  @Autowired
  private SmartstorApiService smartstorApiService;
  @Autowired
  private StorageHostRepository storageHostRepository;
  @Autowired
  private DataSyncServiceImpl dataSyncService;
  @Autowired
  private DataCacheManager dataCacheManager;

  public void addDisk(DiskAddCommand diskAddCommand) {
    log.info("Add disk: {}", diskAddCommand.getDevName());
    exsitStorageHost(diskAddCommand.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.addDisk(diskAddCommand.getServiceIp(), diskAddCommand.getDevName(),
      diskAddCommand.getPartitionCount(), diskAddCommand.getDiskType());
    chkResponseFailed(pbDataResponseCode, "Add disk failed");
    dataSyncService.syncDisks(diskAddCommand.getServiceIp());
  }

  public void delDisk(DiskDelCommand diskDelCommand) {
    log.info("Del disk: {}", diskDelCommand.getDiskName());
    exsitStorageHost(diskDelCommand.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.delDisk(diskDelCommand.getServiceIp(), diskDelCommand.getDiskName());
    chkResponseFailed(pbDataResponseCode, "Delete disk failed");
    dataSyncService.syncDisks(diskDelCommand.getServiceIp());
  }

  public void diskLedOn(DiskRaidLedCommand command) {
    log.info("Disk led on: {}", command.getCesAddr());
    exsitStorageHost(command.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.diskRaidLedOnState(command.getServiceIp(), command.getCesAddr());
    chkResponseFailed(pbDataResponseCode, "Disk led on failed");
    dataSyncService.syncDisks(command.getServiceIp());
  }

  public void diskLedOff(DiskRaidLedCommand command) {
    log.info("Disk led off:{}", command.getCesAddr());
    exsitStorageHost(command.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.diskRaidLedOffState(command.getServiceIp(), command.getCesAddr());
    chkResponseFailed(pbDataResponseCode, "Disk led off failed");
    dataSyncService.syncDisks(command.getServiceIp());
  }

  public void changeRaidLedState(DiskRaidLedCommand command) {
    exsitStorageHost(command.getServiceIp());
    if (command.isLedOn()) {
      diskLedOn(command);
    } else {
      diskLedOff(command);
    }
    saveLedStateToCache(command);
  }

  private void saveLedStateToCache(DiskRaidLedCommand command) {
    try {
      CachedData<DiskRaidLedInfo> cachedData =
        dataCacheManager.gets(command.getServiceIp(), DiskRaidLedInfo.class);
      DiskRaidLedInfo raidLedInfo = makeDiskRaidLedInfo(command);
      if (cachedData == null) {
        dataCacheManager.save(command.getServiceIp(), Lists.newArrayList(raidLedInfo), DiskRaidLedInfo.class);
      } else {
        List<DiskRaidLedInfo> data = cachedData.getData();
        Map<String, DiskRaidLedInfo> diskLedUuidMap = data
          .stream()
          .collect(Collectors.toMap(DiskRaidLedInfo::getDiskUuid, Function.identity(), (a, b) -> b));
        if (diskLedUuidMap.containsKey(raidLedInfo.getDiskUuid())) {
          DiskRaidLedInfo raidInCache = diskLedUuidMap.get(raidLedInfo.getDiskUuid());
          BeanUtils.copyProperties(raidLedInfo, raidInCache);
        } else {
          data.add(raidLedInfo);
        }
        dataCacheManager.save(command.getServiceIp(), data, DiskRaidLedInfo.class);
      }
    } catch (Exception e) {
      log.warn("==============>Save led state to cache failed");
    }
  }

  private DiskRaidLedInfo makeDiskRaidLedInfo(DiskRaidLedCommand command) {
    DiskRaidLedInfo raidLedInfo = new DiskRaidLedInfo();
    raidLedInfo.setListenIp(command.getServiceIp());
    raidLedInfo.setCesAddr(command.getCesAddr());
    raidLedInfo.setDiskRaidLedOpt(DiskRaidLedOperateEnum.getOpt(command.isLedOn()));
    raidLedInfo.setDiskRaidLedState(DiskRaidLedStateEnum.getState(command.isLedOn()));
    return raidLedInfo;
  }

  private void exsitStorageHost(String serviceIp) {
    if (storageHostRepository.findByServiceIp(serviceIp) == null) {
      throw new StorageHostNotFoundException();
    }
  }

  private void chkResponseFailed(PbDataResponseCode pbDataResponseCode, String errMsgPrefix) {
    if (pbDataResponseCode == null) {
      log.warn(errMsgPrefix + ": no response data");
      throw new PbdataDiskException("No response data");
    }
    if (!pbDataResponseCode.isOk()) {
      String msg = errMsgPrefix.concat(": ").concat(pbDataResponseCode.getMessage());
      log.warn("Exce failed:" + msg);
      throw new PbdataDiskException(msg);
    }
  }

}
