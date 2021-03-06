package smartmon.smartstor.infra.remote.exception;

import smartmon.smartstor.domain.exception.SmartStorErrno;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.utilities.general.SmartMonException;

public class PbDataBadResponseException extends SmartMonException {
  private static final long serialVersionUID = -7204614056778694028L;

  public PbDataBadResponseException(PbDataResponseCode responseCode) {
    super(SmartStorErrno.PBDATA_BAD_RESPONSE, responseCode.getMessage());
  }
}
