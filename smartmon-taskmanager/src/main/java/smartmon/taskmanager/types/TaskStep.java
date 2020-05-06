package smartmon.taskmanager.types;

import lombok.Getter;

@Getter
public class TaskStep {
  private static final String STRATEGY_CONTINUE = "continue";
  private static final String STRATEGY_BREAK = "break";

  private final String strategy;
  private final Runnable step;

  public TaskStep(String strategy, Runnable step) {
    this.strategy = strategy;
    this.step = step;
  }

  public TaskStep(Runnable step) {
    this(STRATEGY_BREAK, step);
  }
}
