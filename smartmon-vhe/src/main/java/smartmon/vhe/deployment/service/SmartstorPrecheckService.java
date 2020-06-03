package smartmon.vhe.deployment.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.vhe.deployment.command.SmartstorPrecheckCommand;

@Slf4j
@Service
public class SmartstorPrecheckService extends SmartstorService {
  @Autowired
  private TaskManagerService taskManagerService;

  public TaskGroup precheck(List<SmartstorPrecheckCommand> commands) {
    List<TaskDescription> tasks = commands.stream().map(this::taskDescription).collect(Collectors.toList());
    TaskGroup taskGroup = taskManagerService.createTaskGroup("PrecheckSmartstor", tasks);
    taskManagerService.invokeTaskGroupParallel(taskGroup);
    return taskGroup;
  }

  private TaskDescription taskDescription(SmartstorPrecheckCommand command) {
    Runnable uploadInstaller = () -> uploadInstaller(command, command.getSmartstorInstaller());
    Runnable precheck = () -> doSmartstorCommand(command.getAddress(), command.getSmartstorInstaller(),
      command.getSmartstorTemplate());
    return new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_PRECHECK).withResource(TaskRes.RES_SMARTSTOR).withParameters(command)
      .withStep("UPLOAD", "upload installer", uploadInstaller)
      .withStep("PRECHECK", "precheck environment", precheck)
      .build();
  }

  @Override
  protected boolean isPrecheck() {
    return true;
  }
}
