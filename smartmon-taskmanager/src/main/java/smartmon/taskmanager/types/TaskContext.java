package smartmon.taskmanager.types;

import lombok.Getter;
import lombok.Setter;

public class TaskContext {
  public static final int TASK_INIT = 0;
  public static final int TASK_RUNNING = 10;
  public static final int TASK_COMPLETED = 50;

  @Getter
  private final Long taskId;

  @Getter
  private final String name;

  @Getter
  @Setter
  private int status = TASK_INIT;

  @Setter
  @Getter
  private Object detail;

  public TaskContext(Long taskId, String name) {
    this.taskId = taskId;
    this.name = name;
  }
}
