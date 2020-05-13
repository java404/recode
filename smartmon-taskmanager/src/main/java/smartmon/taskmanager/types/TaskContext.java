package smartmon.taskmanager.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import smartmon.utilities.misc.JsonConverter;

@Data
@ToString
public class TaskContext {
  private static final ThreadLocal<TaskContext> currentContext = new ThreadLocal<TaskContext>();

  public static final String STATUS_PENDING = "pending";
  public static final String STATUS_RUNNING = "running";
  public static final String STATUS_COMPLETED = "completed";

  private Long taskId;
  private Long taskGroupId;
  private String option;

  @ToString.Exclude
  private List<TaskStep> steps;

  @ToString.Exclude
  private TaskStep currentStep;

  private Date createTime = new Date();
  private Date completeTime = new Date();
  private String status = STATUS_PENDING;

  private boolean success = true;
  private Object detail;
  private String detailContent;
  private String error;

  private int totalSteps = 0;
  private int completedSteps = 0;

  public TaskContext() {
    /* NOP */
  }

  public TaskContext(Long taskGroupId, TaskDescription description) {
    this.taskGroupId = taskGroupId;
    this.option = description.getOption().dump();
    this.steps = description.getSteps();
  }

  public void setTaskError(Exception error) {
    setError(error.toString());
  }

  public static void setCurrentContext(TaskContext context) {
    currentContext.set(context);
  }

  public static TaskContext getCurrentContext() {
    return currentContext.get();
  }

  public static TaskOption getCurrentTaskOption() {
    final TaskContext context = getCurrentContext();
    return TaskOption.make(context == null ? null : context.getOption());
  }

  public static <T> T getCurrentTaskOption(Class<T> valueType) {
    final TaskOption option = getCurrentTaskOption();
    return (option == null || option.getData() == null)
      ? null : JsonConverter.treeToValueQuietly(option.getData(), valueType);
  }

  public void flush() {
    detailContent = JsonConverter.writeValueAsStringQuietly(detail);
  }
}
