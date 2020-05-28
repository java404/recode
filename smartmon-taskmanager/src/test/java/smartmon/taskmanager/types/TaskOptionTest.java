package smartmon.taskmanager.types;

import org.junit.Assert;
import org.junit.Test;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;

public class TaskOptionTest {
  private static class TestParameter {
    private String data = "abc";
    private int num = 100;

    public String getData() {
      return data;
    }

    public void setData(String data) {
      this.data = data;
    }

    public int getNum() {
      return num;
    }

    public void setNum(int num) {
      this.num = num;
    }
  }

  @Test
  public void createTaskOption() {
    final TestParameter parameter = new TestParameter();
    final TaskOption taskOption = TaskOption.make(TaskAct.ACT_EXAMPLE, TaskRes.RES_EXAMPLE, parameter);
    final TestParameter parameter2 = taskOption.getParameters(TestParameter.class);
    Assert.assertEquals(parameter.getData(), parameter2.getData());
    Assert.assertEquals(parameter.getNum(), parameter2.getNum());
  }
}