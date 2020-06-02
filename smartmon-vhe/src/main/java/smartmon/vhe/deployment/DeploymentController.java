package smartmon.vhe.deployment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.deployment.command.SmartstorPrecheckCommand;

@Api(tags = "deployment")
@RestController
@RequestMapping("${smartmon.api.prefix:/vhe/api/v2}/deployment")
public class DeploymentController {
  @Autowired
  private SmartstorService smartstorService;

  @ApiOperation("get smartstor template")
  @GetMapping("smartstor/installers/{fileId}/template")
  public SmartMonResponse<String> getSmartstorTemplate(@PathVariable(value = "fileId") Long fileId) {
    return new SmartMonResponse<>(smartstorService.getTemplate(fileId));
  }

  @ApiOperation("smartstor precheck")
  @PostMapping("smartstor/precheck")
  public SmartMonResponse<TaskGroupVo> precheckSmartstor(@RequestBody List<SmartstorPrecheckCommand> commands) {
    TaskGroup taskGroup = smartstorService.precheck(commands);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }
}
