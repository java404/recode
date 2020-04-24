package smartmon.vhe.exception;

import smartmon.utilities.general.SmartMonException;
import smartmon.vhe.config.SmartMonVheErrno;

public class FeignClientException extends SmartMonException {

  public FeignClientException(String message) {
    super(SmartMonVheErrno.FEIGN_EXEC_FAILED, message);
  }
}
