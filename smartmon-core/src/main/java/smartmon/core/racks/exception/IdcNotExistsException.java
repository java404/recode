package smartmon.core.racks.exception;

import smartmon.core.config.SmartMonErrno;
import smartmon.utilities.general.SmartMonException;

public class IdcNotExistsException extends SmartMonException {
  private static final long serialVersionUID = -5823968567769281575L;

  public IdcNotExistsException(String message) {
    super(SmartMonErrno.IDC_NOT_EXISTS, message);
  }
}
