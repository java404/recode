package smartmon.taskmanager.types;

import java.util.List;
import lombok.Getter;

@Getter
public class TaskDescription {
  private final TaskOption option;
  private final List<TaskStep> steps;

  public TaskDescription(TaskOption option, List<TaskStep> steps) {
    this.option = option;
    this.steps = steps;
  }
}
