package smartmon.core.ipmi;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.hosts.mapper.SmartMonHostMapper;
import smartmon.core.hosts.SmartMonHost;

@Slf4j
@Service
public class IpmiService {
  @Autowired
  private SmartMonHostMapper smartMonHostMapper;

  public void syncIpmiInfo(ExecutorService executorService) {
    List<SmartMonHost> smartMonHosts = smartMonHostMapper.selectAll();
    List<Callable<Void>> callableList = smartMonHosts.stream()
      .filter(SmartMonHost::ipmiEnabled)
      .map(smartMonHost -> (Callable<Void>) () -> {
        syncIpmiInfo(smartMonHost);
        return null;
      }).collect(Collectors.toList());
    try {
      executorService.invokeAll(callableList);
    } catch (Exception err) {
      log.error("sync ipmi info interrupted", err);
    }
  }

  private void syncIpmiInfo(SmartMonHost smartMonHost) {
    try {
      PowerStateEnum powerState;
      synchronized (smartMonHost.getIpmiAddress().intern()) {
        powerState = IpmiManager.getPowerState(smartMonHost.getIpmiAddress(),
          smartMonHost.getIpmiUsername(), smartMonHost.getIpmiPassword());
      }
      SmartMonHost smartMonHostLatest = smartMonHostMapper.selectByPrimaryKey(smartMonHost.getHostUuid());
      if (smartMonHostLatest != null) {
        smartMonHostLatest.setPowerState(powerState);
        Date date = new Date();
        smartMonHostLatest.setUpdateTime(date);
        smartMonHostLatest.setStatusTime(date);
        smartMonHostMapper.updateByPrimaryKey(smartMonHostLatest);
      }
    } catch (Exception err) {
      log.error(String.format("Sync ipmi info error, host[%s]", smartMonHost.getManageIp()), err);
    }
  }
}

