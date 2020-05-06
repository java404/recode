package smartmon.utilities.encryption;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class SmartMonEncryption implements Encryption {
  private static final int KEY_GEN_INIT = 128;
  private final String key;
  private final String algorithm;

  public SmartMonEncryption(String key, String algorithm) throws NoSuchAlgorithmException {
    this.key = key;
    this.algorithm = algorithm;
    final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(KEY_GEN_INIT);
  }

  @Override
  public byte[] encryptBytes(byte[] src) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException {
    final Cipher cipher = Cipher.getInstance(this.algorithm);
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
    return cipher.doFinal(src);
  }

  @Override
  public String encrypt(String content) throws IllegalBlockSizeException,
      InvalidKeyException, BadPaddingException,
      NoSuchAlgorithmException, NoSuchPaddingException {
    return Strings.isNullOrEmpty(content) ? "" :
      Base64.encodeBase64String(encryptBytes(content.getBytes(Charsets.UTF_8)));
  }

  @Override
  public byte[] decryptBytes(byte[] src) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException {
    final Cipher cipher = Cipher.getInstance(this.algorithm);
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
    return cipher.doFinal(src);
  }

  @Override
  public String decrypt(String content) throws IllegalBlockSizeException,
      InvalidKeyException, BadPaddingException,
      NoSuchAlgorithmException, NoSuchPaddingException {
    return Strings.isNullOrEmpty(content) ? "" :
      StringUtils.newStringUtf8(decryptBytes(Base64.decodeBase64(content)));
  }
}
