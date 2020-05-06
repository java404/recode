package smartmon.taskmanager.types;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TaskGroup {
  public static final String STATUS_PENDING = "pending";
  public static final String STATUS_RUNNING = "running";
  public static final String STATUS_COMPLETED = "completed";

  private final Long taskGroupId;
  private final String name;
  private final String service;

  private final List<TaskContext> tasks;

  @Setter
  private String status = STATUS_PENDING;

  @Setter
  private boolean taskError = false;

  public TaskGroup(Long taskGroupId, String service, String name, List<TaskContext> tasks) {
    this.taskGroupId = taskGroupId;
    this.name = name;
    this.service = service;
    this.tasks = tasks;
  }
}
