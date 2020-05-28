package smartmon.examples;

import java.util.Arrays;
import org.junit.Ignore;
import org.junit.Test;

public class QuickSortTest {
  private void qsort(int[] data) {
    // TODO
  }

  @Ignore
  @Test
  public void test() {
    final int[] data = new int[]{11, 2, 7, 8, 15};
    System.out.println("before sort: " + Arrays.toString(data));
    qsort(data);
    System.out.println("after sort: " + Arrays.toString(data));
  }
}
