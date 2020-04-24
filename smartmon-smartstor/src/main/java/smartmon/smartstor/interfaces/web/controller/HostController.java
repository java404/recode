package smartmon.smartstor.interfaces.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.smartstor.app.HostAppService;
import smartmon.smartstor.app.command.HostInitCommand;
import smartmon.smartstor.interfaces.web.controller.vo.HostInitVo;
import smartmon.smartstor.interfaces.web.representation.HostRepresentationService;
import smartmon.smartstor.interfaces.web.representation.dto.HostsScanDto;
import smartmon.smartstor.interfaces.web.representation.vo.HostVerifyVo;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.types.TaskContext;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.webtools.page.SmartMonPageParams;

@Api(tags = "hosts")
@RequestMapping("api/v2/hosts")
@RestController
public class HostController {
  @Autowired
  private HostAppService hostAppService;
  @Autowired
  private HostRepresentationService hostRepresentationService;
  @Autowired
  private TaskManagerService taskManagerService;

  @GetMapping
  @SmartMonPageParams
  public SmartMonResponse getHosts() {
    return new SmartMonResponse<>(hostRepresentationService.getStorageHosts());
  }

  @GetMapping("scan")
  public SmartMonResponse scanHosts(@RequestParam("serviceIps") String serviceIps) {
    Set<String> ips = Arrays.stream(serviceIps.split(",")).collect(Collectors.toSet());
    TaskContext taskContext = taskManagerService.createTask("ScanHosts");
    Runnable runnable = () -> {
      List<HostsScanDto> hostsScanDtos = hostRepresentationService.scanHosts(ips);
      taskContext.setDetail(hostsScanDtos);
    };
    taskManagerService.invokeTask(taskContext, runnable);
    return new SmartMonResponse<>(taskContext);
  }

  @ApiOperation("Verify host config")
  @PostMapping("/verify")
  public SmartMonResponse<String> apiVerify(@RequestBody HostVerifyVo vo) {
    hostAppService.verifyHost(vo.toHostVerifyCommand());
    return SmartMonResponse.OK;
  }

  @ApiOperation("Init hosts")
  @PostMapping("init")
  public SmartMonResponse<String> init(@RequestBody List<HostInitVo> hostsVo) {
    hostAppService.initHosts(HostInitCommand.convertToCommand(hostsVo));
    return SmartMonResponse.OK;
  }
}