package smartmon.smartstor.infra.remote.types;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PbDataResponseCode {
  private int retcode;
  private String message;

  public boolean isOk() {
    return retcode == 0;
  }
}
