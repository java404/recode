package smartmon.gateway.config;

public class SmartMonErrno {
  private static final int BEGIN_NUM = 5000;

  public static final int FILE_UPLOAD_ERROR = BEGIN_NUM + 1;
  public static final int FEIGN_EXEC_FAILED = BEGIN_NUM + 2;
}
