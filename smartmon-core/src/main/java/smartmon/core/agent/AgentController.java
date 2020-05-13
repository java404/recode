package smartmon.core.agent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "agents")
@RequestMapping("${smartmon.apiPrefix:/core/api/v2}/agents")
@RestController
public class AgentController {
  @Autowired
  private AgentService agentService;

  @ApiOperation("install agent")
  @PatchMapping
  public SmartMonResponse<TaskGroup> installAgent(@RequestParam("hostUuid") String hostUuid) {
    TaskGroup taskGroup = agentService.installInjector(hostUuid);
    return new SmartMonResponse<>(taskGroup);
  }
}
