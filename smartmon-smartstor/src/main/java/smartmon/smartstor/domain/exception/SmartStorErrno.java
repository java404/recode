package smartmon.smartstor.domain.exception;

public class SmartStorErrno {
  public static final int BEGIN_NUM = 1500;

  public static final int PBDATA_NO_RESPONSE = BEGIN_NUM + 1;
  public static final int PBDATA_INVALID_RESPONSE = BEGIN_NUM + 2;
  public static final int PBDATA_BAD_RESPONSE = BEGIN_NUM + 3;
  public static final int API_VERSION_NOT_FOUND = BEGIN_NUM + 4;
  public static final int API_VERSION_UNSUPPORTED = BEGIN_NUM + 5;
  public static final int STORAGE_HOST_VERIFY_FAILED = BEGIN_NUM + 6;
  public static final int PBDATA_NO_BODY = BEGIN_NUM + 7;
  public static final int PBDATA_DECODE = BEGIN_NUM + 8;
  public static final int PBDATA_GROUP_OPT_FAILED = BEGIN_NUM + 9;
  public static final int PBDATA_DISK_OPT_FAILED = BEGIN_NUM + 10;
  public static final int PBDATA_LUN_OPT_FAILED = BEGIN_NUM + 11;
  public static final int PBDATA_POOL_OPT_FAILED = BEGIN_NUM + 12;
  public static final int FEIGN_EXEC_FAILED = BEGIN_NUM + 13;
}
