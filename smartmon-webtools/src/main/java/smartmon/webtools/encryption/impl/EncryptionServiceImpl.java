package smartmon.webtools.encryption.impl;

import java.security.NoSuchAlgorithmException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smartmon.utilities.encryption.Encryption;
import smartmon.utilities.encryption.SmartMonEncryption;
import smartmon.webtools.encryption.EncryptionService;

@Slf4j
@Service
public class EncryptionServiceImpl implements EncryptionService {
  private static final String DEFAULT_ALGORITHM = "AES/ECB/PKCS5Padding";
  private Encryption encryption;

  @Value("${smartmon.security.encryptionKey:Y0dobFoyUmhRREl3TVRnd09HdGxlUT09}")
  private String smartmonEncryptionKey;

  private String decryptSeedKey(String key) {
    final String firstDecode = StringUtils.newStringUtf8(Base64.decodeBase64(key));
    return StringUtils.newStringUtf8(Base64.decodeBase64(firstDecode));
  }

  /** Creating the encryption instance. */
  @PostConstruct
  public void init() throws NoSuchAlgorithmException {
    log.info("Loading encryption: {} - {}", DEFAULT_ALGORITHM, smartmonEncryptionKey);
    this.encryption = new SmartMonEncryption(decryptSeedKey(smartmonEncryptionKey), DEFAULT_ALGORITHM);
  }

  @Override
  public Encryption getEncryption() {
    return encryption;
  }
}
