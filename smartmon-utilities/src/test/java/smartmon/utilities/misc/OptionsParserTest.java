package smartmon.utilities.misc;

import java.io.File;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class OptionsParserTest {
  @Test
  public void test() {
    final String testContent = "age=40\ndata=20";
    final OptionsParser parser = new OptionsParser(testContent);
    final String age = parser.get("age");
    Assert.assertEquals(age, "40");
    final String data = parser.get("data");
    Assert.assertEquals(data, "20");
    final String bad = parser.get("bad");
    Assert.assertNull(bad);
  }

  @Ignore
  @Test
  public void testFile() {
    final File src = new File("d:/tmp/opts.txt");
    final OptionsParser parser = new OptionsParser(src);
    final String age = parser.get("age");
    final String data = parser.get("data");
    final String bad = parser.get("bad");
    Assert.assertNull(bad);
  }
}