package smartmon.core.hosts;

import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.core.hosts.types.HostAddCommand;
import smartmon.core.hosts.types.HostConfigCommand;
import smartmon.core.hosts.types.MonitorNetInterfaceVo;
import smartmon.core.hosts.types.SmartMonHost;
import smartmon.core.hosts.types.SmartMonHostDetailVo;
import smartmon.core.hosts.types.SmartMonHostVo;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.BeanConverter;
import smartmon.utilities.misc.JsonConverter;
import smartmon.webtools.page.SmartMonPageParams;
import smartmon.webtools.page.SmartMonPageResponseBuilder;

@Api(tags = "hosts")
@RestController
@RequestMapping("${smartmon.apiPrefix:/core/api/v2}/hosts")
public class HostsController {
  @Autowired
  private HostsService hostsService;
  @Autowired
  private TaskManagerService taskManagerService;

  @ApiOperation("get host list")
  @GetMapping
  @SmartMonPageParams
  public SmartMonResponse<Page<SmartMonHostVo>> get(ServerHttpRequest request) {
    List<SmartMonHost> smartMonHosts = hostsService.getAll();
    List<SmartMonHostVo> smartMonHostVos = BeanConverter.copy(smartMonHosts, SmartMonHostVo.class);
    return new SmartMonPageResponseBuilder<>(smartMonHostVos, request, "createTime").build();
  }

  @ApiOperation("get host detail info")
  @GetMapping("{hostUuid}")
  public SmartMonResponse<SmartMonHostDetailVo> getHostDetail(@PathVariable("hostUuid") String hostUuid) {
    SmartMonHost smartMonHost = hostsService.getHostById(hostUuid);
    SmartMonHostDetailVo vo = BeanConverter.copy(smartMonHost, SmartMonHostDetailVo.class);
    if (vo != null) {
      List<String> monitorNetInterfaces = hostsService.getMonitorNetInterfaces(smartMonHost);
      vo.setMonitorInterfaces(monitorNetInterfaces);
    }
    return new SmartMonResponse<>(vo);
  }

  @ApiOperation("add host")
  @PostMapping
  public SmartMonResponse<SmartMonHost> addHost(@RequestBody HostAddCommand hostAddCommand) {
    return new SmartMonResponse<>(hostsService.addHost(hostAddCommand));
  }

  @ApiOperation("config host")
  @PutMapping("{hostUuid}/config")
  public SmartMonResponse<TaskGroupVo> configHost(
    @PathVariable("hostUuid") String hostUuid, @RequestBody HostConfigCommand hostConfigCommand) {
    Map<String, Object> parameters = getHostConfigParameters(hostUuid, hostConfigCommand);
    TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_CONFIG).withResource(TaskRes.RES_HOST).withParameters(parameters)
      .withStep("CONFIG", "config host", () -> hostsService.configHost(hostUuid, hostConfigCommand))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("ConfigHost", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  private Map<String, Object> getHostConfigParameters(String hostUuid, HostConfigCommand hostConfigCommand) {
    Map<String, Object> parameters = BeanConverter.beanToMap(hostConfigCommand);
    parameters.put("hostUuid", hostUuid);
    parameters.remove("sysPassword");
    parameters.remove("ipmiPassword");
    return parameters;
  }

  @ApiOperation("get host monitor net interfaces")
  @GetMapping("{hostUuid}/monitor-net-interfaces")
  public SmartMonResponse<MonitorNetInterfaceVo> getMonitorNetInterfaces(@PathVariable("hostUuid") String hostUuid) {
    MonitorNetInterfaceVo vo = hostsService.getMonitorNetInterfaces(hostUuid);
    return new SmartMonResponse<>(vo);
  }

  @ApiOperation("config host monitor net interfaces")
  @PatchMapping("{hostUuid}/monitor-net-interfaces")
  public SmartMonResponse<TaskGroupVo> configMonitorNetInterfaces(
    @PathVariable("hostUuid") String hostUuid, @RequestBody List<String> monitorNetInterfaces) {
    Map<String, String> parameters = Maps.newHashMap();
    parameters.put("hostUuid", hostUuid);
    parameters.put("monitorInterfaces", JsonConverter.writeValueAsStringQuietly(monitorNetInterfaces));
    TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_CONFIG).withResource(TaskRes.RES_MONITOR_INTERFACES).withParameters(parameters)
      .withStep("CONFIG", "config monitor interfaces",
        () -> hostsService.configMonitorNetInterfaces(hostUuid, monitorNetInterfaces))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("ConfigMonitorInterfaces", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }
}
