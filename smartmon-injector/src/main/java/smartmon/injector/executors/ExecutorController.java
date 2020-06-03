package smartmon.injector.executors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.injector.config.SmartMonBatchConfig;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;

@Slf4j
@RequestMapping("${smartmon.api.prefix:/injector/api/v2}/executors")
@RestController
public class ExecutorController {
  @Autowired
  private ExecutorService executorService;
  @Autowired
  private TaskManagerService taskManagerService;
  @Autowired
  private SmartMonBatchConfig smartMonBatchConfig;

  @PostMapping("shell-command")
  public String executeShellCommand(@RequestBody String command) {
    String commandExecute = String.format("cd %s && %s", smartMonBatchConfig.getFileUploadTargetPath(), command);
    Runnable executeShellCommand = () -> {
      LogOutputProcessor processor = new LogOutputProcessorInStepContext();
      executorService.executeShellCommand(commandExecute, processor);
    };
    TaskDescription taskDescription = new TaskDescriptionBuilder()
      .withAction("EXECUTE").withResource("SHELL_COMMAND").withParameters(command)
      .withStep("EXECUTE", "execute shell command", executeShellCommand)
      .build();
    TaskGroup taskGroup = taskManagerService.createTaskGroup("ExecuteShellCommand", taskDescription);
    taskManagerService.invokeTaskGroup(taskGroup);
    return taskGroup.getTaskGroupId().toString();
  }

  @DeleteMapping("dirs")
  public Boolean deleteTempDir(@RequestParam("dir") String dir) {
    if (StringUtils.isEmpty(dir)) {
      return false;
    }
    String filepath = smartMonBatchConfig.getFileUploadTargetPath();
    String command = String.format("cd %s && ls | grep -w %s && rm -rvf %s  || echo '%s not found'",
      filepath, dir, dir, dir);
    executorService.executeShellCommand(command);
    return true;
  }
}
