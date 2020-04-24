package smartmon.smartstor.interfaces.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.types.TaskContext;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "tasks")
@RequestMapping("api/v2/tasks")
@RestController
public class TaskController {
  @Autowired
  private TaskManagerService taskManagerService;

  @ApiOperation("Get task context by task id")
  @GetMapping("{id}")
  public SmartMonResponse<TaskContext> getByTaskId(@PathVariable Long id) {
    return new SmartMonResponse<>(taskManagerService.findById(id));
  }
}
