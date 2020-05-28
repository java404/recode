package smartmon.gateway.uploader.exception;

import smartmon.gateway.config.SmartMonErrno;
import smartmon.utilities.general.SmartMonException;

public class SmartMonUploaderError extends SmartMonException {
  private static final long serialVersionUID = 8262432218206868344L;

  public SmartMonUploaderError() {
    super(SmartMonErrno.FILE_UPLOAD_ERROR);
  }
}
