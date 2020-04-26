package smartmon.core.hosts;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.core.hosts.types.HostAddCommand;
import smartmon.core.hosts.types.SmartMonHost;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "hosts")
@RestController
@RequestMapping("${smartmon.apiPrefix:/core/api/v2}/hosts")
public class HostsController {
  @Autowired
  private HostsService hostsService;

  @ApiOperation("get host list")
  @GetMapping
  public SmartMonResponse<List<SmartMonHost>> get() {
    return new SmartMonResponse<>(hostsService.getAll());
  }

  @ApiOperation("add host batch")
  @PostMapping("addhosts")
  public SmartMonResponse<List<SmartMonHost>> addHosts(@RequestBody List<HostAddCommand> hostAddCommands) {
    return new SmartMonResponse<>(hostsService.addHosts(hostAddCommands));
  }
}
