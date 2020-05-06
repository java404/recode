package smartmon.taskmanager.controller;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.exception.InvalidTaskGroupId;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.utilities.general.SmartMonResponse;


@RestController
@RequestMapping("${smartmon.api.prefix}/tasks")
@Slf4j
public class SmartMonTaskManager {
  @Autowired
  private TaskManagerService taskManagerService;

  @GetMapping
  public SmartMonResponse<List<TaskGroup>> getAllTaskGroups() {
    return new SmartMonResponse<>(taskManagerService.getAllTaskGroups());
  }

  @ApiOperation("Get task group by id")
  @GetMapping("{id}")
  public SmartMonResponse<TaskGroup> getByTaskId(@PathVariable Long id) {
    final TaskGroup taskGroup = taskManagerService.findTaskGroupById(id);
    if (taskGroup == null) {
      log.error("Invalid task group {}", id);
      throw new InvalidTaskGroupId();
    }
    return new SmartMonResponse<>(taskGroup);
  }
}
