package smartmon.utilities.misc;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SmartMonTime {
  public static Date now() {
    return new Date(System.currentTimeMillis());
  }

  public static Date expirationSeconds(long seconds) {
    return new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
  }
}
