package smartmon.utilities.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SmartMonResponse<T> {
  public static final SmartMonResponse<String> OK = new SmartMonResponse<String>(0, null);
  private int errno = 0;
  @JsonInclude(Include.NON_NULL)
  private T content;

  public SmartMonResponse() {
    // NOP
  }

  public SmartMonResponse(int errno, T content) {
    this.errno = errno;
    this.content = content;
  }

  public SmartMonResponse(T content) {
    this(0, content);
  }
}
