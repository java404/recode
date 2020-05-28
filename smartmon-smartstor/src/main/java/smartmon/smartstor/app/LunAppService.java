package smartmon.smartstor.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.app.command.LunActiveStateCommand;
import smartmon.smartstor.app.command.LunAddCommand;
import smartmon.smartstor.app.command.LunDelCommand;
import smartmon.smartstor.app.command.LunStateCommand;
import smartmon.smartstor.domain.exception.PbdataLunException;
import smartmon.smartstor.domain.exception.StorageHostNotFoundException;
import smartmon.smartstor.domain.gateway.DataSyncService;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;

@Slf4j
@Service
public class LunAppService {
  @Autowired
  private SmartstorApiService smartstorApiService;
  @Autowired
  private DataSyncService dataSyncService;
  @Autowired
  private StorageHostRepository storageHostRepository;

  public void addLun(LunAddCommand addCommand) {
    log.info("Add lun:{}", addCommand.getDiskName());
    exsitStorageHost(addCommand.getServiceIp());
    PbDataResponseCode pbDataResponseCode = smartstorApiService.addLun(addCommand.getServiceIp(),
                                                                      addCommand.getDiskName(),
                                                                      addCommand.getBasedisk(),
                                                                      addCommand.getPoolName(),
                                                                      addCommand.getSize(),
                                                                      addCommand.getGroupName());
    chkResponseFailed(pbDataResponseCode, "Add lun failed");
    dataSyncService.syncLuns(addCommand.getServiceIp());
  }

  public void deleteLun(LunDelCommand command) {
    log.info("Delete lun:{}", command.getLunName());
    exsitStorageHost(command.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.delLun(command.getServiceIp(), command.getLunName(), command.getIsLvvote(), command.getIgnoreAsmstatus());
    chkResponseFailed(pbDataResponseCode, "Delete lun failed");
    dataSyncService.syncGroups(command.getServiceIp());
  }


  public void lunOnline(LunStateCommand stateCommand) {
    log.info("Lun online:{}", stateCommand.getLunName());
    exsitStorageHost(stateCommand.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.lunOnline(stateCommand.getServiceIp(), stateCommand.getLunName());
    chkResponseFailed(pbDataResponseCode, "Lun online failed");
    dataSyncService.syncLuns(stateCommand.getServiceIp());
  }

  public void lunOffline(LunStateCommand stateCommand) {
    log.info("Lun offline:{}", stateCommand.getLunName());
    exsitStorageHost(stateCommand.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.lunOffline(stateCommand.getServiceIp(), stateCommand.getLunName(), null);
    chkResponseFailed(pbDataResponseCode, "Lun offline failed");
    dataSyncService.syncLuns(stateCommand.getServiceIp());
  }

  public void changeLunState(LunStateCommand lunStateCommand) {
    if (lunStateCommand.getOnline()) {
      lunOnline(lunStateCommand);
    } else {
      lunOffline(lunStateCommand);
    }
  }

  private void exsitStorageHost(String serviceIp) {
    if (storageHostRepository.findByServiceIp(serviceIp) == null) {
      throw new StorageHostNotFoundException();
    }
  }

  private void chkResponseFailed(PbDataResponseCode pbDataResponseCode, String errMsgPrefix) {
    if (pbDataResponseCode == null) {
      log.warn(errMsgPrefix + ":No response data");
      throw new PbdataLunException("No response data");
    }
    if (!pbDataResponseCode.isOk()) {
      String msg = errMsgPrefix.concat(": ").concat(pbDataResponseCode.getMessage());
      log.warn("Exec failed:" + msg);
      throw new PbdataLunException(msg);
    }
  }

  public void changeActiveState(LunActiveStateCommand command) {
    if (command.isActive()) {
      activeLun(command);
    } else {
      inactiveLun(command);
    }
  }

  public void inactiveLun(LunActiveStateCommand command) {
    log.info("Lun inactive:{}", command.getLunName());
    exsitStorageHost(command.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.lunInactive(command.getServiceIp(), command.getLunName(), null);
    chkResponseFailed(pbDataResponseCode, "Lun inactive failed");
    dataSyncService.syncLuns(command.getServiceIp());
  }

  public void activeLun(LunActiveStateCommand command) {
    log.info("Lun active:{}", command.getLunName());
    exsitStorageHost(command.getServiceIp());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.lunActive(command.getServiceIp(), command.getLunName());
    chkResponseFailed(pbDataResponseCode, "Lun active failed");
    dataSyncService.syncLuns(command.getServiceIp());
  }
}
