package smartmon.smartstor.infra.remote.exception;

import smartmon.smartstor.domain.exception.SmartStorErrno;
import smartmon.utilities.general.SmartMonException;

public class PbDataExceptionInvalidResponse extends SmartMonException {
  private static final long serialVersionUID = -4262906904888690381L;

  public PbDataExceptionInvalidResponse() {
    super(SmartStorErrno.PBDATA_INVALID_RESPONSE);
  }
}
