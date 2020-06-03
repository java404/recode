package smartmon.utilities.misc;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public class SmartMonResultParser {
  private static final String LINE_SEPARATOR = "\n";
  private static final String SMARTMON_RESULT_PREFIX = "Smartmon-result:";

  public static SmartMonResult parseAndConvertSmartMonResult(String output) {
    String smartMonResult = SmartMonResultParser.parseSmartMonResult(output);
    return convert(smartMonResult);
  }

  public static String parseSmartMonResult(String output) {
    return Arrays.stream(output.split(LINE_SEPARATOR))
      .filter(line -> line.startsWith(SMARTMON_RESULT_PREFIX))
      .map(line -> line.substring(SMARTMON_RESULT_PREFIX.length()).trim())
      .findFirst().orElse(null);
  }

  private static SmartMonResult convert(String smartMonResult) {
    if (StringUtils.isEmpty(smartMonResult)) {
      return null;
    }
    return JsonConverter.readValueQuietly(smartMonResult, SmartMonResult.class);
  }
}
