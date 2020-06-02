package smartmon.falcon.remote.exception;

public class FalconError {
  private static final int BEGIN_NUM = 3000;

  public static final int FALCON_NO_RESPONSE = BEGIN_NUM + 1;
  public static final int FALCON_BAD_RESPONSE = BEGIN_NUM + 2;
  public static final int FALCON_INVALID_RESPONSE = BEGIN_NUM + 3;

  public static final int FALCON_SEND_EMAIL = BEGIN_NUM + 4;
}
