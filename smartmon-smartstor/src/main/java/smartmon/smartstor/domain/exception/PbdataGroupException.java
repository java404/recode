package smartmon.smartstor.domain.exception;

import smartmon.utilities.general.SmartMonException;

public class PbdataGroupException extends SmartMonException {
  private static final long serialVersionUID = 4991206962917713310L;

  public PbdataGroupException(String msg) {
    super(SmartStorErrno.PBDATA_GROUP_OPT_FAILED, msg);
  }
}
