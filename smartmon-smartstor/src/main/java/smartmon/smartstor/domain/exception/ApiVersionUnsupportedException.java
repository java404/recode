package smartmon.smartstor.domain.exception;

import smartmon.utilities.general.SmartMonException;

public class ApiVersionUnsupportedException extends SmartMonException {
  private static final long serialVersionUID = -7302567620357476724L;

  public ApiVersionUnsupportedException(String message) {
    super(SmartStorErrno.API_VERSION_UNSUPPORTED, message);
  }
}
