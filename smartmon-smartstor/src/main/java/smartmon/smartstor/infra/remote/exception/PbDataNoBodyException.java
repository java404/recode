package smartmon.smartstor.infra.remote.exception;

import smartmon.smartstor.domain.exception.SmartStorErrno;
import smartmon.utilities.general.SmartMonException;

public class PbDataNoBodyException extends SmartMonException {
  private static final long serialVersionUID = -5978922886491723345L;

  public PbDataNoBodyException() {
    super(SmartStorErrno.PBDATA_NO_BODY);
  }
}
