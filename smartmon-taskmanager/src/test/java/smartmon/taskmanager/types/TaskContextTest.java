package smartmon.taskmanager.types;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.record.TaskStatus;
import smartmon.taskmanager.record.TaskStepType;

public class TaskContextTest {
  private String step1 = "test";
  private String step2 = "test";

  public static class TaskArg {
    private String data = "abc";

    public String getData() {
      return data;
    }

    public void setData(String data) {
      this.data = data;
    }
  }

  private void step1() {
    final TaskContext context = TaskContext.currentTaskContext();
    final TaskStep step = context.getCurrentStep();
    final TaskArg taskArg = context.getTaskOption().getParameters(TaskArg.class);
    step1 = step1 + ";" + taskArg.getData() + ";step1";
    step.appendLog("test");
    step.setDetail(taskArg);
  }

  private void step2() {
    final TaskContext context = TaskContext.currentTaskContext();
    final TaskStep step = context.getCurrentStep();
    final TaskArg taskArg = context.getTaskOption().getParameters(TaskArg.class);
    step2 = step2 + ";" + taskArg.getData() + ";step2";
    step.setDetail(taskArg);
    step.appendLog("test");
    context.setDetail(taskArg);
  }

  @Test
  public void test() {
    final TaskOption taskOption = TaskOption.make(TaskAct.ACT_EXAMPLE,
      TaskRes.RES_EXAMPLE, new TaskArg());
    final TaskStep taskStep1 = new TaskStep(TaskStepType.STEP_EXAMPLE, "Example", this::step1);
    final TaskStep taskStep2 = new TaskStep(TaskStepType.STEP_EXAMPLE, "Example", this::step2);
    final TaskDescription taskDescription = new TaskDescription(taskOption, Arrays.asList(taskStep1, taskStep2));
    final TaskContext context = new TaskContext(1L, taskDescription);
    TaskContext.run(context);
    Assert.assertEquals(step1, "test;abc;step1");
    Assert.assertEquals(step2, "test;abc;step2");
    Assert.assertEquals(context.getStatus(), TaskStatus.COMPLETED);
  }
}
