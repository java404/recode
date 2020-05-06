package smartmon.falcon.remote.exception;

import smartmon.utilities.general.SmartMonException;

public class FalconExceptionInvalidResponse extends SmartMonException {
  private static final long serialVersionUID = 7875926966423436940L;

  public FalconExceptionInvalidResponse() {
    super(FalconError.FALCON_INVALID_RESPONSE);
  }
}
