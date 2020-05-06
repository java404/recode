package smartmon.examples.controller;

import io.swagger.annotations.Api;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "tasks")
@RestController
@RequestMapping("${smartmon.api.prefix:/api/v2}/task")
@Slf4j
public class TaskManager {
  @Autowired
  private TaskManagerService taskManagerService;

  @GetMapping("create")
  public SmartMonResponse<String> create() {
    taskManagerService.invokeTask("example", () -> {
      log.debug("test task");
    });
    return SmartMonResponse.OK;
  }

  @Data
  public static class Details {
    private String data1 = "data1";
    private int data2 = 100;
  }

  public void taskJob(TaskContext taskContext) {
    log.debug("test task 2");
    taskContext.setDetail(new Details());
  }

  @GetMapping("create2")
  public SmartMonResponse<String> create2() {
    taskManagerService.invokeTask("example", () -> {
      final TaskContext taskContext = TaskContext.getCurrentContext();
      taskJob(taskContext);
    });
    return SmartMonResponse.OK;
  }

  @GetMapping("all-group")
  public List<TaskGroup> getAllGroups() {
    return taskManagerService.getAllTaskGroups();
  }
}
