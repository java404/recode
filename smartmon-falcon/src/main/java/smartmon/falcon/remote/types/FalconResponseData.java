package smartmon.falcon.remote.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FalconResponseData {
  @JsonProperty("message")
  private String successMessage;
  @JsonProperty("error")
  private String errorMessage;
  private int code;

  public int getCode() {
    return this.errorMessage == null ? 0 : -1;
  }
}
