package smartmon.utilities.encryption;

import com.google.common.base.Strings;
import org.apache.commons.codec.digest.DigestUtils;

public class EncryptionUtil {
  /** String content to MD5 Hex string. */
  public static String md5hex(String content) {
    return Strings.isNullOrEmpty(content) ? Strings.nullToEmpty(content) : DigestUtils.md5Hex(content);
  }
}
