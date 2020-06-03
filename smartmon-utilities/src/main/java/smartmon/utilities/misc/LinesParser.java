package smartmon.utilities.misc;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections4.ListUtils;

public class LinesParser {
  private static String[] lineToColumns(String line, String columnSplitPattern) {
    return Splitter.onPattern(columnSplitPattern).trimResults()
      .omitEmptyStrings().splitToList(Strings.nullToEmpty(line)).toArray(new String[]{});
  }

  public static <T> List<T> parse(String lines, String columnSplitPattern,
                                  Function<? super String[], ? extends T> mapper) {
    return Splitter.onPattern("\r?\n")
      .trimResults().omitEmptyStrings()
      .splitToList(Strings.nullToEmpty(lines)).stream()
      .filter(i -> i != null && !Strings.isNullOrEmpty(i.trim()))
      .map(line -> mapper.apply(lineToColumns(line, columnSplitPattern)))
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }

  public static String filter(String lines, Function<? super String, ? extends String> mapper) {
    final List<String> results = Splitter.onPattern("\r?\n")
      .trimResults().omitEmptyStrings()
      .splitToList(Strings.nullToEmpty(lines)).stream()
      .filter(i -> i != null && !Strings.isNullOrEmpty(i.trim()))
      .map(mapper)
      .filter(Objects::nonNull).collect(Collectors.toList());
    return String.join("", ListUtils.emptyIfNull(results));
  }

  public static String matchGroup(String content, String regex, int groupId) {
    final Pattern pattern = Pattern.compile(Strings.nullToEmpty(regex));
    final Matcher matcher = pattern.matcher(Strings.nullToEmpty(content));
    return (!matcher.find() || matcher.groupCount() < groupId) ? "" : matcher.group(groupId);
  }
}
