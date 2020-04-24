package smartmon.core.racks.exception;

import smartmon.core.config.SmartMonErrno;
import smartmon.utilities.general.SmartMonException;

public class IdcRenameNotAllowedException extends SmartMonException {
  private static final long serialVersionUID = -6543471121381407980L;

  public IdcRenameNotAllowedException(String message) {
    super(SmartMonErrno.IDC_RENAME_NOT_ALLOWED, message);
  }
}
