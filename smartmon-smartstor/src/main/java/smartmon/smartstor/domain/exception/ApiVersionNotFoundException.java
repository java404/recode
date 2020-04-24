package smartmon.smartstor.domain.exception;

import smartmon.utilities.general.SmartMonException;

public class ApiVersionNotFoundException extends SmartMonException {
  private static final long serialVersionUID = 3309808040080356977L;

  public ApiVersionNotFoundException(String message) {
    super(SmartStorErrno.API_VERSION_NOT_FOUND, message);
  }
}
