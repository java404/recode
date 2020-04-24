package smartmon.utilities.misc;

import org.junit.Test;

public class TargetHostTest {
  @Test
  public void testBuilder() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.8", 22).build();
    System.out.println(targetHost.toString());
  }
}