package smartmon.examples.controller;

import io.swagger.annotations.Api;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.record.TaskStepType;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.types.TaskStep;
import smartmon.taskmanager.vo.TaskGroupVo;
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

  @Data
  public static class TaskArg {
    private String data = "arg1";
  }

  public void taskJob1() {
    final TaskContext taskContext = TaskContext.currentTaskContext();
    final TaskStep currentStep = taskContext.getCurrentStep();

    log.debug("Running task 1");
    currentStep.appendLog("Log line 1");
    currentStep.appendLog("Log line 2");

    taskContext.setDetail(new TaskDetails());
    currentStep.setDetail(new StepDetails());
  }

  public void taskJob2() {
    final TaskContext taskContext = TaskContext.currentTaskContext();
    final TaskStep currentStep = taskContext.getCurrentStep();

    log.debug("Running task 2");
    currentStep.appendLog("Log line 1, Task 2");
    try {
      Thread.sleep(1000 * 3);
    } catch (InterruptedException error) {
      log.warn("example task sleep error", error);
    }
    currentStep.appendLog("Log line 2, Task 2");
    taskContext.setDetail(new TaskDetails());
    currentStep.setDetail(new StepDetails());
    throw new RuntimeException("test error");
  }

  @GetMapping("create")
  public SmartMonResponse<TaskGroupVo> create() {
    final TaskDescription desc = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_EXAMPLE)
      .withResource(TaskRes.RES_EXAMPLE)
      .withParameters(new TaskArg())
      .withStep(TaskStepType.STEP_EXAMPLE, "step1", this::taskJob1)
      .withStep(TaskStepType.STEP_EXAMPLE, "step2", this::taskJob2)
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("example", desc);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }


  @GetMapping("all-group")
  public List<TaskGroupVo> getAllGroups() {
    return taskManagerService.getAllTaskGroups();
  }


  @GetMapping("group/{id}")
  public TaskGroupVo getGroup(@PathVariable("id")  Long id) {
    return taskManagerService.findTaskGroupById(id);
  }
}
