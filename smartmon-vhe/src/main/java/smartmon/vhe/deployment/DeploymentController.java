package smartmon.vhe.deployment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonException;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.LocalNetworkInterface;
import smartmon.vhe.config.SmartMonVheErrno;
import smartmon.vhe.deployment.command.SmartstorDeployCommand;
import smartmon.vhe.deployment.command.SmartstorPrecheckCommand;

@Slf4j
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

  @ApiOperation("smartstor deploy")
  @PostMapping("smartstor/deploy")
  public SmartMonResponse<TaskGroupVo> deploySmartstor(@RequestBody List<SmartstorDeployCommand> commands) {
    TaskGroup taskGroup = smartstorService.deploy(commands);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("judge local ip")
  @PostMapping("local-ip-judge")
  public SmartMonResponse<List<String>> judgeLocalIp(@RequestBody List<String> ips) {
    LocalNetworkInterface localNetworkInterface = new LocalNetworkInterface();
    List<String> localIps = CollectionUtils.emptyIfNull(ips).stream()
      .filter(localNetworkInterface::isLocalIp)
      .collect(Collectors.toList());
    return new SmartMonResponse<>(localIps);
  }

  @ApiOperation(value = "reboot local host")
  @PostMapping("localhost/reboot")
  public SmartMonResponse<Boolean> rebootLocalHost(@RequestParam(value = "ip") String ip) {
    LocalNetworkInterface localNetworkInterface = new LocalNetworkInterface();
    boolean isLocalIp = localNetworkInterface.isLocalIp(ip);
    if (!isLocalIp) {
      throw new SmartMonException(SmartMonVheErrno.REBOOT_HOST_NOT_ALLOWED,
        String.format("[%s] is not local ip, reboot is not allowed", ip));
    }
    try {
      log.warn("reboot host [{}]", ip);
      String[] commands = {"/bin/sh", "-c", "reboot"};
      Process process = Runtime.getRuntime().exec(commands);
      process.waitFor();
      process.destroy();
    } catch (IOException | InterruptedException err) {
      log.error("reboot host error", err);
      throw new SmartMonException(SmartMonVheErrno.REBOOT_HOST_ERROR, "reboot host error, " + err.getMessage());
    }
    return new SmartMonResponse<>(0, true);
  }
}
