package smartmon.utilities.general;

import lombok.Getter;

@Getter
public class SmartMonException extends RuntimeException {
  private static final long serialVersionUID = 7592823985130504050L;
  private final int errno;
  private final String message;

  public SmartMonException(int errno) {
    this(errno, null);
  }

  public SmartMonException(int errno, String message) {
    super(message);
    this.errno = errno;
    this.message = message;
  }
}
