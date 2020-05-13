package smartmon.utilities.ssh;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public class ShellResult {
  private final Integer code;
  private final String output;

  public boolean isOk() {
    return Objects.equal(0, code);
  }

  public String getOutput() {
    return Strings.nullToEmpty(output).replaceAll("\n$", StringUtils.EMPTY);
  }
}
