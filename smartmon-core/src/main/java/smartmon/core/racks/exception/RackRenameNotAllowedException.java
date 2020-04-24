package smartmon.core.racks.exception;

import smartmon.core.config.SmartMonErrno;
import smartmon.utilities.general.SmartMonException;

public class RackRenameNotAllowedException extends SmartMonException {
  private static final long serialVersionUID = -3610795391721065693L;

  public RackRenameNotAllowedException(String message) {
    super(SmartMonErrno.RACK_RENAME_NOT_ALLOWED, message);
  }
}
