package smartmon.utilities.misc;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class StringItems {
  public static List<String> parse(String src) {
    return Splitter.on(",").trimResults().omitEmptyStrings()
      .splitToList(Strings.nullToEmpty(src));
  }

  public static String[] parseArray(String src) {
    return parse(src).toArray(new String[]{});
  }

  public static String join(List<String> items) {
    return StringUtils.join(items, ",");
  }
}
