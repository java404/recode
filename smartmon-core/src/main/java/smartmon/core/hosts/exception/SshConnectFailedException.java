package smartmon.core.hosts.exception;

import smartmon.core.config.SmartMonErrno;
import smartmon.utilities.general.SmartMonException;

public class SshConnectFailedException extends SmartMonException {
  private static final long serialVersionUID = -5451353088982248886L;

  public SshConnectFailedException(String message) {
    super(SmartMonErrno.SSH_CONNECT_FAILED, message);
  }
}
