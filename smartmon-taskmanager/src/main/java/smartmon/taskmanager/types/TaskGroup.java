package smartmon.taskmanager.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.apache.commons.collections4.ListUtils;
import smartmon.taskmanager.dto.TaskGroupDto;
import smartmon.taskmanager.record.TaskStatus;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.taskmanager.vo.TaskVo;

@Data
public class TaskGroup {
  private Long taskGroupId;
  private final String service;
  private final String name;

  private Date createTime = new Date();
  private Date completeTime = new Date();
  private String status = TaskStatus.PENDING;
  private boolean success = true;

  private List<TaskContext> tasks;

  public TaskGroup(String service, String name, List<TaskContext> tasks) {
    this.name = name;
    this.service = service;
    this.tasks = ListUtils.emptyIfNull(tasks);
  }

  public TaskGroupDto dumpDto() {
    final TaskGroupDto dto = new TaskGroupDto();
    dto.setTaskGroupId(taskGroupId);
    dto.setService(service);
    dto.setName(name);
    dto.setCreateTime(createTime);
    dto.setCompleteTime(completeTime);
    dto.setStatus(status);
    dto.setSuccess(success);
    return dto;
  }

  public TaskGroupVo dumpVo() {
    final TaskGroupVo vo = new TaskGroupVo();
    vo.setTaskGroupId(taskGroupId);
    vo.setService(service);
    vo.setName(name);
    vo.setCreateTime(createTime);
    vo.setCompleteTime(completeTime);
    vo.setStatus(status);
    vo.setSuccess(success);
    final List<TaskVo> taskVoList = new ArrayList<>();
    for (final TaskContext item : tasks) {
      taskVoList.add(item.dumpVo());
    }
    vo.setTasks(taskVoList);
    return vo;
  }

  public static TaskGroupVo dumpVo(TaskGroupDto taskGroupDto, List<TaskVo> tasks) {
    final TaskGroupVo vo = new TaskGroupVo();
    vo.setTaskGroupId(taskGroupDto.getTaskGroupId());
    vo.setService(taskGroupDto.getService());
    vo.setName(taskGroupDto.getName());
    vo.setCreateTime(taskGroupDto.getCreateTime());
    vo.setCompleteTime(taskGroupDto.getCompleteTime());
    vo.setStatus(taskGroupDto.getStatus());
    vo.setSuccess(taskGroupDto.isSuccess());
    vo.setTasks(tasks);
    return vo;
  }
}
