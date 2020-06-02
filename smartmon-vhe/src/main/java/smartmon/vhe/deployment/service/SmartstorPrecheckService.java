package smartmon.vhe.deployment.service;

import com.google.common.collect.Maps;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smartmon.agent.client.AgentClientService;
import smartmon.core.hosts.RemoteHostCommand;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.misc.TaskSynchronizer;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.taskmanager.vo.TaskStepVo;
import smartmon.vhe.config.SmartMonBatchConfig;
import smartmon.vhe.deployment.command.SmartstorPrecheckCommand;
import smartmon.vhe.service.feign.SmartmonCoreFeignClient;

@Slf4j
@Service
public class SmartstorPrecheckService {
  @Value("${smartmon.store.rootFolder:/opt/smartmon/data}")
  private String storeFolder;

  @Autowired
  private SmartMonBatchConfig smartMonBatchConfig;
  @Autowired
  private TaskManagerService taskManagerService;
  @Autowired
  private AgentClientService agentClientService;
  @Autowired
  private SmartmonCoreFeignClient coreFeignClient;

  public TaskGroup precheck(List<SmartstorPrecheckCommand> commands) {
    List<TaskDescription> tasks = commands.stream().map(this::taskDescription).collect(Collectors.toList());
    TaskGroup taskGroup = taskManagerService.createTaskGroup("PrecheckSmartstor", tasks);
    taskManagerService.invokeTaskGroupParallel(taskGroup);
    return taskGroup;
  }

  private TaskDescription taskDescription(SmartstorPrecheckCommand command) {
    Runnable uploadInstaller = () -> uploadInstaller(command, command.getSmartstorInstaller());
    Runnable precheck = () -> doPrecheck(command.getAddress(), command.getSmartstorInstaller(),
      command.getSmartstorTemplate());
    return new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_PRECHECK).withResource(TaskRes.RES_HOST).withParameters(command)
      .withStep("UPLOAD", "upload installer", uploadInstaller)
      .withStep("PRECHECK", "precheck environment", precheck)
      .build();
  }

  private void uploadInstaller(RemoteHostCommand command, String installer) {
    installInjectorIfNeed(command);
    doUploadInstaller(command.getAddress(), installer);
  }

  private void installInjectorIfNeed(RemoteHostCommand command) {
    try {
      Boolean isInjectorHealthy = agentClientService.isInjectorHealthy(command.getAddress());
      if (Objects.equals(Boolean.TRUE, isInjectorHealthy)) {
        TaskContext.currentTaskContext().getCurrentStep().appendLog("injector already installed");
        return;
      }
    } catch (Exception ignored) {
    }
    try {
      TaskGroupVo taskGroupVo = coreFeignClient.installInjector(command).getContent();
      String taskGroupId = taskGroupVo.getTaskGroupId().toString();
      Supplier<TaskGroupVo> taskResultSupplier = () -> coreFeignClient.getByTaskId(taskGroupId).getContent();
      Consumer<TaskGroupVo> logConsumer = taskGroup -> {
        String logs = taskGroup.getTasks().get(0).getSteps().stream()
          .map(TaskStepVo::getStepLog)
          .filter(StringUtils::isNotEmpty)
          .collect(Collectors.joining());
        TaskContext.currentTaskContext().getCurrentStep().updateLog(logs);
      };
      TaskSynchronizer.awaitCompletion(taskResultSupplier, logConsumer);
    } catch (Exception e) {
      String message = "install injector failed";
      log.error(message, e);
      throw new RuntimeException(message + ": " + e.getMessage());
    }
  }

  private void doUploadInstaller(String serviceIp, String installer) {
    Map<String, File> files = Maps.newHashMap();
    String script = "scripts/install_smartstor.py";
    String filepath = smartMonBatchConfig.getSourceRoot();
    File file = new File(filepath + script);
    files.put(smartMonBatchConfig.getRemoteWorkFolder() + script, file);
    filepath = storeFolder.endsWith("/") ? storeFolder : storeFolder + "/";
    file = new File(filepath + installer);
    files.put(smartMonBatchConfig.getRemoteWorkFolder() + installer, file);
    files.keySet().forEach(filename ->
      TaskContext.currentTaskContext().getCurrentStep().appendLog("upload file: " + filename));
    agentClientService.uploadFiles(serviceIp, files);
  }

  private void doPrecheck(String serviceIp, String installer, String template) {
    String command = getDoPrecheckCommand(installer, template);
    TaskContext.currentTaskContext().getCurrentStep().appendLog("execute command: " + command);
    String taskGroupId = agentClientService.executeShellCommand(serviceIp, command);
    Supplier<TaskGroupVo> taskResultSupplier = () -> agentClientService.getTaskResult(serviceIp, taskGroupId);
    Consumer<TaskGroupVo> logConsumer = taskGroup -> {
      String logs = taskGroup.getTasks().get(0).getSteps().get(0).getStepLog();
      TaskContext.currentTaskContext().getCurrentStep().updateLog(logs);
    };
    TaskSynchronizer.awaitCompletion(taskResultSupplier, logConsumer);
  }

  private String getDoPrecheckCommand(String installer, String template) {
    return String.format("export smartstor_parameter_template='%s' && cd %s && cd scripts "
        + "&& python ./install_smartstor.py ../%s precheck",
      template, smartMonBatchConfig.getRemoteWorkFolder(), installer);
  }
}
