package smartmon.utilities.general;

import org.junit.Test;

public class SmartMonExceptionTest {
  @Test
  public void testBuilder() {
    try {
      throw new SmartMonException(100, "abc");
    } catch (SmartMonException err) {
      System.out.println(String.format("Error: %d, %s", err.getErrno(), err.getMessage()));
    }
  }
}
