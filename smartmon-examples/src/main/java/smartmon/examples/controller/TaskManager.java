package smartmon.examples.controller;

import io.swagger.annotations.Api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.types.TaskOption;
import smartmon.taskmanager.types.TaskStep;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "tasks")
@RestController
@RequestMapping("${smartmon.api.prefix:/api/v2}/task")
@Slf4j
public class TaskManager {
  @Autowired
  private TaskManagerService taskManagerService;

  @Data
  public static class TaskDetails {
    private String data1 = "Task Details";
    private int data2 = 100;
  }

  @Data
  public static class StepDetails {
    private String data1 = "Step Details";
    private int data2 = 100;
  }

  public void taskJob1() {
    final TaskContext taskContext = TaskContext.getCurrentContext();
    final TaskStep currentStep = taskContext.getCurrentStep();

    log.debug("Running task 1");
    currentStep.appendLog("Log line 1");
    currentStep.appendLog("Log line 2");

    taskContext.setDetail(new TaskDetails());
    currentStep.setDetail(new StepDetails());
  }

  public void taskJob2() {
    final TaskContext taskContext = TaskContext.getCurrentContext();
    final TaskStep currentStep = taskContext.getCurrentStep();

    log.debug("Running task 2");
    currentStep.appendLog("Log line 1, Task 2");
    try {
      Thread.sleep(1000 * 60);
    } catch (InterruptedException ignore) {
      log.warn("example task sleep error", ignore);
    }
    currentStep.appendLog("Log line 2, Task 2");
    taskContext.setDetail(new TaskDetails());
    currentStep.setDetail(new StepDetails());
  }

  @GetMapping("create")
  public SmartMonResponse<TaskGroup> create() {
    final TaskStep taskStep1 = new TaskStep(this::taskJob1);
    final TaskStep taskStep2 = new TaskStep(this::taskJob2);

    final List<TaskDescription> tasks = new ArrayList<>();
    final TaskDescription desc = new TaskDescription(TaskOption.EMPTY,
      Arrays.asList(taskStep1, taskStep2));
    tasks.add(desc);

    // Save task group
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("test", tasks);
    // Invoke task group
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup);
  }


  @GetMapping("all-group")
  public List<TaskGroup> getAllGroups() {
    return taskManagerService.getAllTaskGroups();
  }


  @GetMapping("group/{id}")
  public TaskGroup getGroup(@PathVariable("id")  Long id) {
    return taskManagerService.findTaskGroupById(id);
  }
}
