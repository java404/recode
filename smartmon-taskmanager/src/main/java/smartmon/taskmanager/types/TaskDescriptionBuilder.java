package smartmon.taskmanager.types;

import java.util.ArrayList;
import java.util.List;

public class TaskDescriptionBuilder {
  private String action;
  private String resource;
  private Object parameters;
  private final List<TaskStep> steps = new ArrayList<>();

  public TaskDescriptionBuilder withAction(String action) {
    this.action = action;
    return this;
  }

  public TaskDescriptionBuilder withResource(String resource) {
    this.resource = resource;
    return this;
  }

  public TaskDescriptionBuilder withParameters(Object parameters) {
    this.parameters = parameters;
    return this;
  }

  public TaskDescriptionBuilder withStep(String type, String name, Runnable step) {
    steps.add(new TaskStep(type, name, step));
    return this;
  }

  public TaskDescription build() {
    final TaskOption taskOption = TaskOption.make(action, resource, parameters);
    return new TaskDescription(taskOption, steps);
  }
}
