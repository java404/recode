package smartmon.core.config;

public class SmartMonErrno {
  private static final int BEGIN_NUM = 1000;

  public static final int IDC_NOT_EXISTS = BEGIN_NUM + 1;
  public static final int IDC_RENAME_NOT_ALLOWED = BEGIN_NUM + 2;
  public static final int RACK_RENAME_NOT_ALLOWED = BEGIN_NUM + 3;
  public static final int RACK_POSITION_NOT_AVAILABLE = BEGIN_NUM + 4;
  public static final int HOST_ALREADY_IN_RACK = BEGIN_NUM + 5;

  public static final int HOST_NOT_FOUND = BEGIN_NUM + 10;
  public static final int HOST_IP_NOT_MATCH = BEGIN_NUM + 11;

  public static final int SSH_CONNECT_FAILED = BEGIN_NUM + 100;
  public static final int SSH_COMMAND_ERROR = BEGIN_NUM + 101;
  public static final int SSH_COPY_FILE_ERROR = BEGIN_NUM + 102;

  public static final int AGENT_STATE_ERROR = BEGIN_NUM + 150;
}
