package smartmon.vhe.config;

public class SmartMonVheErrno {
  private static final int BEGIN_NUM = 2000;

  public static final int HOST_INIT_FAILED = BEGIN_NUM + 1;
  public static final int FEIGN_EXEC_FAILED = BEGIN_NUM + 3;

  public static final int REBOOT_HOST_NOT_ALLOWED = BEGIN_NUM + 11;
  public static final int REBOOT_HOST_ERROR = BEGIN_NUM + 12;

  public static final int SMARTSTOR_INSTALLER_NOT_FOUND = BEGIN_NUM + 20;
  public static final int GET_SMARTSTOR_ARGS_TEMPLATE_ERROR = BEGIN_NUM + 21;
}
