package smartmon.vhe.exception;

import smartmon.utilities.general.SmartMonException;
import smartmon.vhe.config.SmartMonVheErrno;

public class HostInitException extends SmartMonException {
  private static final long serialVersionUID = -3910467444340106417L;

  public HostInitException(String msg) {
    super(SmartMonVheErrno.HOST_INIT_FAILED, msg);
  }
}
