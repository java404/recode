package smartmon.core.hostinfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.core.hosts.RemoteHostCommand;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.TargetHost;

@Api(tags = "host-info")
@RestController
@RequestMapping("${smartmon.apiPrefix:/core/api/v2}/host-info")
public class HostInfoController {
  @Autowired
  private HostInfoService hostInfoService;

  @ApiOperation("get host network interfaces")
  @PostMapping("network/interfaces")
  public SmartMonResponse<TaskGroupVo> getNetInterfaces(@RequestBody List<ScanNetworkCommand> commands) {
    List<TargetHost> hosts = commands.stream()
      .map(RemoteHostCommand::toTargetHost)
      .collect(Collectors.toList());
    TaskGroup taskGroup = hostInfoService.getNetInterfaces(hosts);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }
}
