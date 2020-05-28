package smartmon.smartstor.interfaces.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import smartmon.smartstor.web.dto.SimpleStorageHostDto;
import smartmon.smartstor.web.dto.StorageHostDto;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.webtools.page.SmartMonPageParams;
import smartmon.webtools.page.SmartMonPageResponseBuilder;

@Api(tags = "hosts")
@RequestMapping("${smartmon.api.prefix:/smartstor/api/v2}/hosts")
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
  public SmartMonResponse<List<StorageHostDto>> getHosts() {
    return new SmartMonResponse<>(hostRepresentationService.getStorageHosts());
  }

  @GetMapping("scan")
  public SmartMonResponse<TaskGroupVo> scanHosts(@RequestParam("serviceIps") String serviceIps) {
    final Set<String> ips = Arrays.stream(serviceIps.split(",")).collect(Collectors.toSet());

    final TaskDescription desc = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_SCAN).withResource(TaskRes.RES_NODE).withParameters(serviceIps)
      .withStep("SCAN", "ScanHost", () -> {
        final TaskContext currentContext = TaskContext.currentTaskContext();
        final List<HostsScanDto> hostsScanDtos = hostRepresentationService.scanHosts(ips);
        currentContext.setDetail(hostsScanDtos);
      }).build();

    final TaskGroup taskGroup = taskManagerService.createTaskGroup("ScanHosts", desc);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  @ApiOperation("Init host")
  @PostMapping("init")
  public SmartMonResponse<String> init(@RequestBody HostInitVo hostInitVo) {
    hostAppService.initHost(hostInitVo.toHostInitCommand());
    return SmartMonResponse.OK;
  }

  @ApiOperation("Get host infos")
  @GetMapping("simple-hosts")
  @SmartMonPageParams
  public SmartMonResponse<Page<SimpleStorageHostDto>> getSimpleHostInfo(ServerHttpRequest request) {
    List<SimpleStorageHostDto> simpleHostInfos = hostRepresentationService.getSimpleHostInfo();
    return new SmartMonPageResponseBuilder<>(simpleHostInfos, request, "listenIp").build();
  }

  @ApiOperation("Get not in group host")
  @GetMapping("{serviceIp}/not-in-group")
  public SmartMonResponse<List<SimpleStorageHostDto>> getSimpleHostNotInGroup(@PathVariable("serviceIp") String serviceIp) {
    List<SimpleStorageHostDto> simpleStorageHostDtos =
      hostRepresentationService.getHostNotInGroup(serviceIp);
    return new SmartMonResponse<>(simpleStorageHostDtos);
  }
}
