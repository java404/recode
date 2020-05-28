package smartmon.utilities.encryption;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public interface Encryption {
  byte[] encryptBytes(byte[] src) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException;

  default byte[] encryptBytesQuietly(byte[] src) {
    try {
      return encryptBytes(src);
    } catch (Exception ignore) {
      return null;
    }
  }

  String encrypt(String content) throws IllegalBlockSizeException,
      InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException;


  default String encryptQuietly(String content) {
    try {
      return encrypt(content);
    } catch (Exception ignore) {
      return "";
    }
  }

  byte[] decryptBytes(byte[] src) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException;

  default byte[] decryptBytesQuietly(byte[] src) {
    try {
      return decryptBytes(src);
    } catch (Exception ignore) {
      return null;
    }
  }

  String decrypt(String content) throws IllegalBlockSizeException,
      InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException;

  default String decryptQuietly(String content) {
    try {
      return decrypt(content);
    } catch (Exception ignore) {
      return content;
    }
  }
}
