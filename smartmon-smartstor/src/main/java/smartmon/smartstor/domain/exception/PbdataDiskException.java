package smartmon.smartstor.domain.exception;

import smartmon.utilities.general.SmartMonException;

public class PbdataDiskException extends SmartMonException {

  private static final long serialVersionUID = 2668531730252930379L;

  public PbdataDiskException(String msg) {
    super(SmartStorErrno.PBDATA_DISK_OPT_FAILED, msg);
  }
}
