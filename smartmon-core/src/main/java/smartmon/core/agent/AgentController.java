package smartmon.core.agent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "agents")
@RequestMapping("${smartmon.apiPrefix:/core/api/v2}/agents")
@RestController
public class AgentController {
  @Autowired
  private AgentService agentService;

  @ApiOperation("install agent")
  @PostMapping
  public SmartMonResponse<TaskGroupVo> installAgent(@RequestParam("hostUuid") String hostUuid) {
    TaskGroup taskGroup = agentService.installAgent(hostUuid);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("uninstall agent")
  @DeleteMapping
  public SmartMonResponse<TaskGroupVo> uninstallAgent(@RequestParam("hostUuid") String hostUuid) {
    TaskGroup taskGroup = agentService.uninstallAgent(hostUuid);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }
}
