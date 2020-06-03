package smartmon.core.hostinfo;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.agent.service.AgentInstallService;
import smartmon.injector.client.AgentClientService;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.utilities.misc.TargetHost;

@Service
public class HostInfoService {
  @Autowired
  private TaskManagerService taskManagerService;
  @Autowired
  private AgentInstallService agentInstallService;
  @Autowired
  private AgentClientService agentClientService;

  public TaskGroup getNetInterfaces(List<TargetHost> hosts) {
    List<TaskDescription> tasks = hosts.stream().map(this::taskDescription).collect(Collectors.toList());
    TaskGroup taskGroup = taskManagerService.createTaskGroup("ScanNetInterfaces", tasks);
    taskManagerService.invokeTaskGroupParallel(taskGroup);
    return taskGroup;
  }

  private TaskDescription taskDescription(TargetHost targetHost) {
    return new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_SCAN).withResource(TaskRes.RES_NETWORK_INTERFACES).withParameters(targetHost)
      .withStep("INSTALL", "install injector", () -> agentInstallService.installInjectorIfNeed(targetHost))
      .withStep("SCAN", "scan network interfaces", () -> getNetInterfacesByServiceIp(targetHost.getAddress()))
      .build();
  }

  private void getNetInterfacesByServiceIp(String serviceIp) {
    String networkInterfaces = agentClientService.getNetInterfaces(serviceIp);
    TaskContext.currentTaskContext().setDetail(networkInterfaces);
  }
}
