package smartmon.smartstor.domain.exception;

import smartmon.utilities.general.SmartMonException;

public class PbdataPoolException extends SmartMonException {
  private static final long serialVersionUID = -7973586506910805650L;

  public PbdataPoolException(String msg) {
    super(SmartStorErrno.PBDATA_POOL_OPT_FAILED, msg);
  }
}
