package smartmon.smartstor.infra.remote.exception;

import smartmon.smartstor.domain.exception.SmartStorErrno;
import smartmon.utilities.general.SmartMonException;

public class PbDataDecodeException extends SmartMonException {
  private static final long serialVersionUID = 8935882410975364628L;

  public PbDataDecodeException(String message) {
    super(SmartStorErrno.PBDATA_DECODE, message);
  }
}
