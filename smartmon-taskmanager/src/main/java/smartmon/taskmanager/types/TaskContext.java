package smartmon.taskmanager.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import smartmon.utilities.misc.JsonConverter;


public class TaskContext {
  private static final ThreadLocal<TaskContext> currentContext = new ThreadLocal<TaskContext>();

  public static final String STATUS_PENDING = "pending";
  public static final String STATUS_RUNNING = "running";
  public static final String STATUS_COMPLETED = "completed";

  @Getter
  private final Long taskId;

  @Getter
  private final Long taskGroupId;

  @Getter
  private final String name;

  @Getter
  private final TaskOption option;

  @Getter
  @JsonIgnore
  private final List<TaskStep> steps;

  @Getter
  @Setter
  private String status = STATUS_PENDING;

  @Setter
  @Getter
  private Object detail;

  @Getter
  @Setter
  private boolean success = true;

  @Getter
  @Setter
  private String error;

  public TaskContext(Long taskId, Long taskGroupId, String name, TaskDescription description) {
    this.taskId = taskId;
    this.taskGroupId = taskGroupId;
    this.name = name;
    this.option = description.getOption();
    this.steps = description.getSteps();
  }

  public void setTaskError(Exception error) {
    // TODO detail error mesages
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
    return context == null ? null : context.getOption();
  }

  public static <T> T getCurrentTaskOption(Class<T> valueType) {
    final TaskOption option = getCurrentTaskOption();
    if (option == null || option.getData() == null) {
      return null;
    }
    return JsonConverter.treeToValueQuietly(option.getData(), valueType);
  }
}
