package smartmon.falcon.remote.exception;

import smartmon.utilities.general.SmartMonException;

public class FalconExceptionNoResponse extends SmartMonException {
  private static final long serialVersionUID = 2210078656697150256L;

  public FalconExceptionNoResponse() {
    super(FalconError.FALCON_NO_RESPONSE);
  }
}
