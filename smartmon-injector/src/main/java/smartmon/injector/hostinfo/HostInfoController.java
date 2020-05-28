package smartmon.injector.hostinfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.core.hosts.SmartMonHostInfo;

@Slf4j
@Api(tags = "host-info")
@RequestMapping("${smartmon.api.prefix:/injector/api/v2}/host")
@RestController
public class HostInfoController {
  @Autowired
  private BasicInfoService basicInfoService;
  @Autowired
  private SystemInfoService systemInfoService;
  @Autowired
  private HardwareInfoService hardwareInfoService;

  @ApiOperation("get host info")
  @GetMapping("info")
  public SmartMonHostInfo getSmartMonHostInfo() {
    SmartMonHostInfo smartMonHostInfo = new SmartMonHostInfo();
    smartMonHostInfo.setBasicInfo(basicInfoService.getBasicInfo());
    smartMonHostInfo.setSystemInfo(systemInfoService.getSystemInfo());
    smartMonHostInfo.setHardwareInfo(hardwareInfoService.getHardwareInfo());
    return smartMonHostInfo;
  }
}
