package smartmon.taskmanager.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.exception.InvalidTaskGroupId;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;


@RestController
@RequestMapping("${smartmon.api.prefix}/tasks")
@Slf4j
public class SmartMonTaskManager {
  @Autowired
  private TaskManagerService taskManagerService;

  @GetMapping
  public SmartMonResponse<List<TaskGroupVo>> getAllTaskGroups() {
    return new SmartMonResponse<>(taskManagerService.getAllTaskGroups());
  }

  @GetMapping("{id}")
  public SmartMonResponse<TaskGroupVo> getByTaskId(@PathVariable("id") Long id) {
    final TaskGroupVo taskGroup = taskManagerService.findTaskGroupById(id);
    if (taskGroup == null) {
      log.error("Invalid task group {}", id);
      throw new InvalidTaskGroupId();
    }
    return new SmartMonResponse<>(taskGroup);
  }
}
