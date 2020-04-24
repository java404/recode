package smartmon.vhe.exception;

import smartmon.utilities.general.SmartMonException;
import smartmon.vhe.config.SmartMonVheErrno;

public class HostInitException extends SmartMonException {

  public HostInitException(String msg) {
    super(SmartMonVheErrno.HOST_INIT_FAILED, msg);
  }
}
