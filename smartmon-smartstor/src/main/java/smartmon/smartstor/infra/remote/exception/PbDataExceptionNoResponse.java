package smartmon.smartstor.infra.remote.exception;

import smartmon.smartstor.domain.exception.SmartStorErrno;
import smartmon.utilities.general.SmartMonException;

public class PbDataExceptionNoResponse extends SmartMonException {
  private static final long serialVersionUID = -1043080954080078677L;

  public PbDataExceptionNoResponse() {
    super(SmartStorErrno.PBDATA_NO_RESPONSE);
  }
}
