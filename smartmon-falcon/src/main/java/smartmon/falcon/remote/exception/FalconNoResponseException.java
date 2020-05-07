package smartmon.falcon.remote.exception;

import smartmon.utilities.general.SmartMonException;

public class FalconNoResponseException extends SmartMonException {
  private static final long serialVersionUID = 2210078656697150256L;

  public FalconNoResponseException() {
    super(FalconError.FALCON_NO_RESPONSE);
  }
}
