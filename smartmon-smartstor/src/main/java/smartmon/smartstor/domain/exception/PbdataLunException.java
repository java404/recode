package smartmon.smartstor.domain.exception;

import smartmon.utilities.general.SmartMonException;

public class PbdataLunException extends SmartMonException {

  private static final long serialVersionUID = 105510563270726802L;

  public PbdataLunException(String msg) {
    super(SmartStorErrno.PBDATA_LUN_OPT_FAILED, msg);
  }
}
