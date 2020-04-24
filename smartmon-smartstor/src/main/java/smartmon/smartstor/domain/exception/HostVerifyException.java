package smartmon.smartstor.domain.exception;

import smartmon.utilities.general.SmartMonException;

public class HostVerifyException extends SmartMonException {
  private static final long serialVersionUID = -6728934787683478986L;

  public HostVerifyException(String message) {
    super(SmartStorErrno.STORAGE_HOST_VERIFY_FAILED, message);
  }
}
