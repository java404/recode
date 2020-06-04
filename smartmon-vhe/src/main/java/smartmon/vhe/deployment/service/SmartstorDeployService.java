package smartmon.vhe.deployment.service;

import com.github.rholder.retry.RetryException;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.hosts.RemoteHostCommand;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.utilities.misc.LocalNetworkInterface;
import smartmon.utilities.misc.RetryUtils;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.ssh.ShellExecute;
import smartmon.utilities.ssh.ShellExecuteEvent;
import smartmon.vhe.deployment.command.SmartstorDeployCommand;
import smartmon.vhe.service.HostService;
import smartmon.vhe.service.dto.HostInitDto;

@Service
public class SmartstorDeployService extends SmartstorService {
  @Autowired
  private TaskManagerService taskManagerService;
  @Autowired
  private HostService hostService;

  public TaskGroup deploy(List<SmartstorDeployCommand> commands) {
    List<TaskDescription> tasks = commands.stream().map(this::taskDescription).collect(Collectors.toList());
    TaskGroup taskGroup = taskManagerService.createTaskGroup("DeploySmartstor", tasks);
    taskManagerService.invokeTaskGroupParallel(taskGroup);
    return taskGroup;
  }

  private TaskDescription taskDescription(SmartstorDeployCommand command) {
    TaskDescriptionBuilder taskDescriptionBuilder = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_INSTALL).withResource(TaskRes.RES_SMARTSTOR).withParameters(command);
    if (StringUtils.isNotEmpty(command.getNetworkParameters())) {
      Runnable configNetwork = () -> configNetwork(command.getAddress(), command.getNetworkParameters());
      taskDescriptionBuilder.withStep("CONFIG", "config network", configNetwork);
    }
    Runnable uploadInstaller = () -> uploadInstaller(command, command.getSmartstorInstaller());
    Runnable install = () -> doSmartstorCommand(command.getAddress(), command.getSmartstorInstaller(),
      command.getSmartstorTemplate());
    taskDescriptionBuilder.withStep("UPLOAD", "upload installer", uploadInstaller)
      .withStep("INSTALL", "install smartstor", install);
    if (StringUtils.isNotEmpty(command.getOpensmParameters())) {
      Runnable configOpensm = () -> configOpensm(command.getAddress(), command.getOpensmParameters());
      taskDescriptionBuilder.withStep("CONFIG", "config opensm", configOpensm);
    }
    taskDescriptionBuilder.withStep("REGIST", "regist host", () -> registHost(command))
      .withStep("INSTALL", "install agent", hostService::installAgent);
    boolean isLocalHost = (new LocalNetworkInterface()).isLocalIp(command.getAddress());
    if (!isLocalHost) {
      taskDescriptionBuilder.withStep("REBOOT", "restart host", () -> restartHost(command));
      taskDescriptionBuilder.withStep("CHECK", "check host", () -> checkHost(command));
    }
    return taskDescriptionBuilder.build();
  }

  private void configNetwork(String serviceIp, String networkParameters) {
    uploadScripts(serviceIp, "scripts/if_config.py");
    doCommandAndWaitForComplete(serviceIp, "cd ./scripts && python ./if_config.py");
  }

  private void configOpensm(String serviceIp, String opensmParameters) {
    uploadScripts(serviceIp, "scripts/opensm_config.py", "scripts/opensm_config.sh");
    doCommandAndWaitForComplete(serviceIp, "cd ./scripts && python ./opensm_config.py");
  }

  private void registHost(SmartstorDeployCommand command) {
    HostInitDto hostInitDto = new HostInitDto();
    hostInitDto.setListenIp(command.getAddress());
    hostInitDto.setSshPort(command.getPort());
    hostInitDto.setSysUsername(command.getUsername());
    hostInitDto.setSysPassword(command.getPassword());
    hostInitDto.setIpmiAddress(command.getIpmiAddress());
    hostInitDto.setIpmiUsername(command.getIpmiUsername());
    hostInitDto.setIpmiPassword(command.getIpmiPassword());
    hostInitDto.setSize(command.getSize());
    hostService.registHost(hostInitDto);
  }

  private void restartHost(RemoteHostCommand remoteHostCommand) {
    String command = "echo 'reboot host' && reboot";
    ShellExecuteEvent shellExecuteEvent = (message) -> {
      TaskContext.currentTaskContext().getCurrentStep().appendLog(message);
    };
    doRemoteCommand(remoteHostCommand.toTargetHost(), command, shellExecuteEvent);
  }

  private void checkHost(RemoteHostCommand remoteHostCommand) {
    String command = "echo 'reboot host success'";
    int retryTimes = 10;
    int sleepSeconds = 30;
    AtomicInteger retriedTimes = new AtomicInteger(0);
    Callable<Boolean> callable = () -> {
      int value = retriedTimes.incrementAndGet();
      String progress = String.format("%s/%s", value, retryTimes);
      TaskContext.currentTaskContext().getCurrentStep().appendLog("check host status... " + progress);
      doRemoteCommand(remoteHostCommand.toTargetHost(), command, null);
      return true;
    };
    Predicate<Boolean> predicate = v -> !Objects.equals(Boolean.TRUE, v);
    try {
      TimeUnit.SECONDS.sleep(sleepSeconds);
      RetryUtils.retryForExceptionAndResult(callable, predicate, retryTimes, sleepSeconds);
    } catch (InterruptedException | ExecutionException | RetryException err) {
      throw new RuntimeException("check host status failed", err);
    }
    TaskContext.currentTaskContext().getCurrentStep().appendLog("check host status success");
  }

  private void doRemoteCommand(TargetHost targetHost, String command, ShellExecuteEvent shellExecuteEvent) {
    try {
      ShellExecute shellExecutor = new ShellExecute(targetHost);
      shellExecutor.setExecuteEvent(shellExecuteEvent);
      shellExecutor.run(command);
    } catch (Exception err) {
      throw new RuntimeException(err);
    }
  }
}
