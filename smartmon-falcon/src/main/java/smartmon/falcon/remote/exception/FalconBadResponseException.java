package smartmon.falcon.remote.exception;

import smartmon.utilities.general.SmartMonException;

public class FalconBadResponseException extends SmartMonException {
  private static final long serialVersionUID = -60359259265455002L;

  public FalconBadResponseException(String error) {
    super(FalconError.FALCON_BAD_RESPONSE, error);
  }


}
