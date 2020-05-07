package smartmon.falcon.remote.exception;

import smartmon.utilities.general.SmartMonException;

public class FalconInvalidResponseException extends SmartMonException {
  private static final long serialVersionUID = 7875926966423436940L;

  public FalconInvalidResponseException() {
    super(FalconError.FALCON_INVALID_RESPONSE);
  }
}
