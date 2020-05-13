package smartmon.taskmanager.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TaskGroup {
  public static final String STATUS_PENDING = "pending";
  public static final String STATUS_RUNNING = "running";
  public static final String STATUS_COMPLETED = "completed";


  private Long taskGroupId;
  private String name;
  private String service;

  private Date createTime = new Date();
  private Date completeTime = new Date();
  private String status = STATUS_PENDING;
  private boolean taskError = false;

  @ToString.Exclude
  private List<TaskContext> tasks;

  public TaskGroup() {
    /* NOP */
  }

  public TaskGroup(String service, String name, List<TaskContext> tasks) {
    this.name = name;
    this.service = service;
    this.tasks = tasks;
  }
}
