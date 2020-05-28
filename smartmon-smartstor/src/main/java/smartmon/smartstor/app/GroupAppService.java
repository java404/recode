package smartmon.smartstor.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.app.command.GroupAddCommand;
import smartmon.smartstor.app.command.GroupDeleteCommand;
import smartmon.smartstor.app.command.GroupLunAddCommand;
import smartmon.smartstor.app.command.GroupNodeAddCommand;
import smartmon.smartstor.app.command.GroupNodeDelCommand;
import smartmon.smartstor.domain.exception.PbdataGroupException;
import smartmon.smartstor.domain.gateway.DataSyncService;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;

@Slf4j
@Service
public class GroupAppService {
  @Autowired
  private SmartstorApiService smartstorApiService;
  @Autowired
  private DataSyncService dataSyncService;

  public void addGroup(GroupAddCommand addCommand) {
    log.info("Add Group: {}", addCommand.getGroupName());
    try {
      smartstorApiService.addGroup(addCommand.getServiceIp(), addCommand.getGroupName());
    } catch (Exception e) {
      String msg = "Add group failed".concat(":").concat(e.getMessage());
      throw new PbdataGroupException(msg);
    }
    dataSyncService.syncGroups(addCommand.getServiceIp());
  }

  public void deleteGroup(GroupDeleteCommand deleteCommand) {
    log.info("Delete group: {}", deleteCommand.getGroupName());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.delGroup(deleteCommand.getServiceIp(), deleteCommand.getGroupName());
    chkResponseFailed(pbDataResponseCode, "Delete group failed");
    dataSyncService.syncGroups(deleteCommand.getServiceIp());
  }

  public void addNodeToGroup(GroupNodeAddCommand nodeAddCommand) {
    log.info("Add node to group: {}", nodeAddCommand.getGroupName());
    PbDataResponseCode pbDataResponseCode = smartstorApiService
      .addNodeToGroup(nodeAddCommand.getServiceIp(), nodeAddCommand.getGroupName(), nodeAddCommand.getNodeName());
    chkResponseFailed(pbDataResponseCode, "Add node failed");
    dataSyncService.syncGroups(nodeAddCommand.getServiceIp());
  }

  public void removeNodeFromGroup(GroupNodeDelCommand nodeDelCommand) {
    log.info("Remove node from group: {}", nodeDelCommand.getGroupName());
    PbDataResponseCode pbDataResponseCode = smartstorApiService
      .removeNodeFromGroup(nodeDelCommand.getServiceIp(), nodeDelCommand.getGroupName(), nodeDelCommand.getNodeName());
    chkResponseFailed(pbDataResponseCode, "Remove node failed");
    dataSyncService.syncGroups(nodeDelCommand.getServiceIp());
  }

  private void chkResponseFailed(PbDataResponseCode pbDataResponseCode, String errMsgPrefix) {
    if (pbDataResponseCode == null) {
      log.warn("No response data");
      throw new PbdataGroupException("No response data");
    }
    if (!pbDataResponseCode.isOk()) {
      String msg = errMsgPrefix.concat(": ").concat(pbDataResponseCode.getMessage());
      log.warn("Exec failed: {}", msg);
      throw new PbdataGroupException(msg);
    }
  }

  public void addLunToGroup(GroupLunAddCommand addCommand) {
    log.info("Add lun to group: {}", addCommand.getGroupName());
    PbDataResponseCode pbDataResponseCode =
      smartstorApiService.addLunToGroup(addCommand.getServiceIp(), addCommand.getGroupName(), addCommand.getLunName());
    chkResponseFailed(pbDataResponseCode, "Add lun to group failed");
    dataSyncService.syncGroups(addCommand.getServiceIp());
  }
}
