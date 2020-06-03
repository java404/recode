package smartmon.utilities.misc;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmartMonResult {
  private Boolean result;
  private String message;
  private Object metadata;

  public boolean isOk() {
    return Objects.equals(Boolean.TRUE, result);
  }
}
