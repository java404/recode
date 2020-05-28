package smartmon.injector.executors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.injector.files.FileService;
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
  private FileService fileService;

  @PostMapping("shell-command")
  public String executeShellCommand(@RequestBody String command) {
    String commandExecute = String.format("cd %s && %s", fileService.getTargetFolder(), command);
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
}
