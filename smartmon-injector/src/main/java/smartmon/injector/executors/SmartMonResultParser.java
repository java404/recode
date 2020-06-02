package smartmon.injector.executors;

import java.util.Arrays;

public class SmartMonResultParser {
  private static final String LINE_SEPARATOR = "\n";
  private static final String SMARTMON_RESULT_PREFIX = "Smartmon-result:";

  public static String parseSmartMonResult(String output) {
    return Arrays.stream(output.split(LINE_SEPARATOR))
      .filter(line -> line.startsWith(SMARTMON_RESULT_PREFIX))
      .map(line -> line.substring(SMARTMON_RESULT_PREFIX.length()).trim())
      .findFirst().orElse(null);
  }
}
