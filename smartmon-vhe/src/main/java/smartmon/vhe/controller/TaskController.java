package smartmon.vhe.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.types.TaskContext;
import smartmon.utilities.general.SmartMonResponse;

@RestController
@RequestMapping("${smartmon.api.prefix:/vhe/api/v2}/tasks")
public class TaskController {
  @Autowired
  private TaskManagerService taskManagerService;

  @ApiOperation("Get task context by task id")
  @GetMapping("{id}")
  public SmartMonResponse<TaskContext> getByTaskId(@PathVariable Long id) {
    return new SmartMonResponse<>(taskManagerService.findById(id));
  }
}
