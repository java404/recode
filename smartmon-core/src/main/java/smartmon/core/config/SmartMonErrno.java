package smartmon.core.config;

public class SmartMonErrno {
  private static final int BEGIN_NUM = 1000;

  public static final int IDC_NOT_EXISTS = BEGIN_NUM + 1;
  public static final int IDC_RENAME_NOT_ALLOWED = BEGIN_NUM + 2;
  public static final int RACK_RENAME_NOT_ALLOWED = BEGIN_NUM + 3;
  public static final int RACK_POSITION_NOT_AVAILABLE = BEGIN_NUM + 4;
  public static final int HOST_ALREADY_IN_RACK = BEGIN_NUM + 5;
}
