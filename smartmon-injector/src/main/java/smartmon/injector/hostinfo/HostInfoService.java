package smartmon.injector.hostinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.hosts.SmartMonHostInfo;
import smartmon.injector.hostinfo.services.BasicInfoService;
import smartmon.injector.hostinfo.services.HardwareInfoService;
import smartmon.injector.hostinfo.services.NetworkInfoService;
import smartmon.injector.hostinfo.services.SystemInfoService;

@Service
public class HostInfoService {
  @Autowired
  private BasicInfoService basicInfoService;
  @Autowired
  private SystemInfoService systemInfoService;
  @Autowired
  private HardwareInfoService hardwareInfoService;
  @Autowired
  private NetworkInfoService networkInfoService;

  public SmartMonHostInfo getSmartMonHostInfo() {
    SmartMonHostInfo smartMonHostInfo = new SmartMonHostInfo();
    smartMonHostInfo.setBasicInfo(basicInfoService.getBasicInfo());
    smartMonHostInfo.setSystemInfo(systemInfoService.getSystemInfo());
    smartMonHostInfo.setHardwareInfo(hardwareInfoService.getHardwareInfo());
    smartMonHostInfo.setNetworkInfo(networkInfoService.getNetworkInfo());
    return smartMonHostInfo;
  }
}
