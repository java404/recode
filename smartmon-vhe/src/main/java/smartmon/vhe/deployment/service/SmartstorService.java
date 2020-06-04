package smartmon.vhe.deployment.service;

import com.google.common.collect.Maps;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import smartmon.core.hosts.RemoteHostCommand;
import smartmon.injector.client.AgentClientService;
import smartmon.taskmanager.misc.TaskSynchronizer;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.taskmanager.vo.TaskStepVo;
import smartmon.utilities.misc.SmartMonResult;
import smartmon.utilities.misc.SmartMonResultParser;
import smartmon.vhe.config.SmartMonBatchConfig;
import smartmon.vhe.service.feign.SmartmonCoreFeignClient;

@Slf4j
public abstract class SmartstorService {
  @Value("${smartmon.store.rootFolder:/opt/smartmon/data}")
  private String storeFolder;

  @Autowired
  protected SmartMonBatchConfig smartMonBatchConfig;
  @Autowired
  protected AgentClientService agentClientService;
  @Autowired
  protected SmartmonCoreFeignClient coreFeignClient;

  protected void uploadInstaller(RemoteHostCommand command, String installer) {
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
    uploadScripts(serviceIp, "scripts/install_smartstor.py");
    uploadInstaller(serviceIp, installer);
  }

  protected void uploadScripts(String serviceIp, String... scripts) {
    for (String script : scripts) {
      int index = script.lastIndexOf("/");
      String filepath = index < 0 ? Strings.EMPTY : script.substring(0, index);
      String filename = index < 0 ? script : script.substring(index + 1);
      File file = new File(smartMonBatchConfig.getSourceRoot() + filepath, filename);
      TaskContext.currentTaskContext().getCurrentStep().appendLog("upload file: " + filename);
      try {
        agentClientService.uploadFile(serviceIp, smartMonBatchConfig.getRemoteWorkFolder() + filepath, filename, file);
      } catch (Exception err) {
        log.error("upload script failed", err);
        throw new RuntimeException("upload script failed:" + err.getMessage());
      }
    }
  }

  protected void uploadInstaller(String serviceIp, String installer) {
    Map<String, File> files = Maps.newHashMap();
    String filepath = storeFolder.endsWith("/") ? storeFolder : storeFolder + "/";
    String installerPath = filepath + installer;
    File file = new File(installerPath);
    TaskContext.currentTaskContext().getCurrentStep().appendLog("upload installer: " + installer);
    try {
      agentClientService.uploadFile(serviceIp, smartMonBatchConfig.getRemoteWorkFolder(), installer, file);
    } catch (IOException err) {
      throw new RuntimeException(err);
    }
  }

  protected void doSmartstorCommand(String serviceIp, String installer, String template) {
    String command = getSmartstorCommand(installer, template);
    doCommandAndWaitForComplete(serviceIp, command);
  }

  protected void doCommandAndWaitForComplete(String serviceIp, String command) {
    try {
      command = String.format("cd %s && %s", smartMonBatchConfig.getRemoteWorkFolder(), command);
      TaskContext.currentTaskContext().getCurrentStep().appendLog("execute command: " + command);
      String taskGroupId = agentClientService.executeShellCommand(serviceIp, command);
      Supplier<TaskGroupVo> taskResultSupplier = () -> agentClientService.getTaskResult(serviceIp, taskGroupId);
      AtomicReference<String> logsReference = new AtomicReference<>(StringUtils.EMPTY);
      Consumer<TaskGroupVo> logConsumer = taskGroup -> {
        String message = taskGroup.getTasks().get(0).getSteps().get(0).getStepLog();
        if (StringUtils.isNotEmpty(message)) {
          String logs = logsReference.get();
          String logsIncrement = message.substring(logs.length());
          if (StringUtils.isNotEmpty(logsIncrement)) {
            logsIncrement = logsIncrement.substring(0, logsIncrement.length() - 1);
            TaskContext.currentTaskContext().getCurrentStep().appendLog(logsIncrement);
            logsReference.set(message);
          }
        }
      };
      TaskSynchronizer.awaitCompletion(taskResultSupplier, logConsumer);
      String logs = TaskContext.currentTaskContext().getCurrentStep().dumpLog();
      SmartMonResult smartMonResult = SmartMonResultParser.parseAndConvertSmartMonResult(logs);
      if (smartMonResult != null && !smartMonResult.isOk()) {
        throw new RuntimeException(smartMonResult.getMessage());
      }
    } finally {
      deleteTempDir(serviceIp);
    }
  }

  private String getSmartstorCommand(String installer, String template) {
    String command = String.format("export smartstor_parameter_template='%s' && cd ./scripts "
        + "&& python ./install_smartstor.py ../%s",
      template, installer);
    if (isPrecheck()) {
      command += " precheck";
    }
    return command;
  }

  protected boolean isPrecheck() {
    return false;
  }

  protected void deleteTempDir(String serviceIp) {
    try {
      String dir = smartMonBatchConfig.getRemoteWorkFolder();
      dir = dir.endsWith("/") ? dir.substring(0, dir.length() - 1) : dir;
      agentClientService.deleteTempDir(serviceIp, dir);
    } catch (Exception ignored) {
    }
  }
}
