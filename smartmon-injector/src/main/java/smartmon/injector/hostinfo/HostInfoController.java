package smartmon.injector.hostinfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.core.hosts.SmartMonHostInfo;
import smartmon.injector.hostinfo.services.NetworkInfoService;

@Slf4j
@Api(tags = "host-info")
@RequestMapping("${smartmon.api.prefix:/injector/api/v2}/host")
@RestController
public class HostInfoController {
  @Autowired
  private HostInfoService hostInfoService;
  @Autowired
  private NetworkInfoService networkInfoService;

  @ApiOperation("get host info")
  @GetMapping("info")
  public SmartMonHostInfo getSmartMonHostInfo() {
    return hostInfoService.getSmartMonHostInfo();
  }

  @ApiOperation("get network interfaces")
  @GetMapping("network/interfaces")
  public String getNetInterfaces() {
    return networkInfoService.getNetInterfaces();
  }
}
