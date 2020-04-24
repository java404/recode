package smartmon.core.racks.exception;

import smartmon.core.config.SmartMonErrno;
import smartmon.utilities.general.SmartMonException;

public class RackPositionUnavailableException extends SmartMonException {
  private static final long serialVersionUID = 2966727769216998187L;

  public RackPositionUnavailableException(String message) {
    super(SmartMonErrno.RACK_POSITION_NOT_AVAILABLE, message);
  }
}
