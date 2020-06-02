package smartmon.utilities.misc;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LinesParser {
  private static String[] lineToColumns(String line, String columnPattern) {
    return Splitter.onPattern(columnPattern).trimResults()
      .omitEmptyStrings().splitToList(Strings.nullToEmpty(line)).toArray(new String[]{});
  }

  public static <T> List<T> parse(String content, String columnPattern,
                                  Function<? super String[], ? extends T> mapper) {
    return Splitter.onPattern("\r?\n")
      .trimResults().omitEmptyStrings()
      .splitToList(Strings.nullToEmpty(content)).stream()
      .map(line -> mapper.apply(lineToColumns(line, columnPattern)))
      .collect(Collectors.toList());
  }
}
