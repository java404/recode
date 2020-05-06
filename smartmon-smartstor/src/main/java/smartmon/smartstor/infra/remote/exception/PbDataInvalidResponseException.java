package smartmon.smartstor.infra.remote.exception;

import smartmon.smartstor.domain.exception.SmartStorErrno;
import smartmon.utilities.general.SmartMonException;

public class PbDataInvalidResponseException extends SmartMonException {
  private static final long serialVersionUID = -4262906904888690381L;

  public PbDataInvalidResponseException() {
    super(SmartStorErrno.PBDATA_INVALID_RESPONSE);
  }
}
