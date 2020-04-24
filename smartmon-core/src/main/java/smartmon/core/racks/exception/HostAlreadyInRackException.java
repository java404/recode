package smartmon.core.racks.exception;

import smartmon.core.config.SmartMonErrno;
import smartmon.utilities.general.SmartMonException;

public class HostAlreadyInRackException extends SmartMonException {
  private static final long serialVersionUID = -2625072400687622630L;

  public HostAlreadyInRackException(String message) {
    super(SmartMonErrno.HOST_ALREADY_IN_RACK, message);
  }
}
