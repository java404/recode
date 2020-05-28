package smartmon.taskmanager.types;

import org.junit.Assert;
import org.junit.Test;

public class TaskGroupTest {
  @Test
  public void test() {
    final TaskGroup group = new TaskGroup("test", "Test", null);
    Assert.assertEquals(group.getName(), "Test");
  }
}
