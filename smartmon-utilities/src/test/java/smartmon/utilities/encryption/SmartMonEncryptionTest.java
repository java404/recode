package smartmon.utilities.encryption;

import java.security.NoSuchAlgorithmException;
import org.junit.Test;

public class SmartMonEncryptionTest {
  @Test
  public void testEncryption() throws NoSuchAlgorithmException {
    final String testKey = "phegda@201808key";
    final String testAlgorithm = "AES/ECB/PKCS5Padding";
    final Encryption encryption = new SmartMonEncryption(testKey, testAlgorithm);
    try {
      // admin => hGy4/sNnArEYtJKYPXfczg==
      // root123 => TvsvqB1/KZ2PKds+o91uAQ==
      final String content = "root123";
      final String result = encryption.encrypt(content);
      System.out.println("result (encrypt): " + result);
      final String resultContent = encryption.decrypt(result);
      System.out.println("result (decrypt): " + resultContent);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }
}