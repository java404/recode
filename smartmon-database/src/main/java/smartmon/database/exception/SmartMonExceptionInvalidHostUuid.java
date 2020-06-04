package smartmon.database.exception;

import smartmon.utilities.general.SmartMonException;

public class SmartMonExceptionInvalidHostUuid extends SmartMonException {
  public SmartMonExceptionInvalidHostUuid() {
    super(SmartMonErrno.INVALID_HOST_UUID);
  }
}
