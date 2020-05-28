package smartmon.injector.executors;

import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskStep;

public class LogOutputProcessorInStepContext implements LogOutputProcessor {
  private final TaskStep taskStep;

  public LogOutputProcessorInStepContext() {
    taskStep = TaskContext.currentTaskContext().getCurrentStep();
  }

  @Override
  public void processLine(String line) {
    taskStep.appendLog(line);
  }
}
