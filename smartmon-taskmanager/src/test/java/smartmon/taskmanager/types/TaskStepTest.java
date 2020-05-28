package smartmon.taskmanager.types;

import org.junit.Assert;
import org.junit.Test;
import smartmon.taskmanager.record.TaskStepType;

public class TaskStepTest {
  private String data = "test";

  @Test
  public void test() {
    final Runnable step1 = () -> {
      data = "step1";
    };
    final TaskStep step = new TaskStep(TaskStepType.STEP_EXAMPLE, "Sample Step", step1);
    step.getStep().run();
    Assert.assertEquals(data, "step1");
  }
}