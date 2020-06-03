package smartmon.utilities.misc;

import java.util.List;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Assert;
import org.junit.Test;

public class LinesParserTest {
  private static class Item {
    private String data;
    private int num;

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
  public void testSplitter() {
    final String testLines = "Item1  1\n  \n Item2 2";
    final Function<String[], @Nullable Item> col = input -> {
      final Item item = new Item();
      assert input != null;
      item.setData(input[0]);
      item.setNum(Integer.parseInt(input[1]));
      return item;
    };

    final List<Item> items = LinesParser.parse(testLines, "\\s", col);
    Assert.assertEquals(items.size(), 2);
  }

  @Test
  public void testFilter() {
    final String testLines = "   \n   SQL*Plus: ffff \n   \n";
    final String result = LinesParser.filter(testLines, line -> line.startsWith("SQL*Plus:") ? line : null);
    Assert.assertEquals(result, "SQL*Plus: ffff");
  }

  @Test
  public void testMatch() {
    final String src = "   \n   SQL*Plus: Release 12.1.0.2.0 Production  \n   \n";
    final String version = LinesParser.matchGroup(src, "(\\d+\\.\\d+\\.\\d+\\.\\d+\\.\\d+)", 1);
    Assert.assertEquals(version, "12.1.0.2.0");
  }
}
