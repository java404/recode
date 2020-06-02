package smartmon.falcon.remote.exception;

import smartmon.utilities.general.SmartMonException;

public class FalconSendEmailException extends SmartMonException {
  private static final long serialVersionUID = 1696161789973702236L;

  public FalconSendEmailException(String message) {
    super(FalconError.FALCON_SEND_EMAIL, message);
  }
}
