package smartmon.examples.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.utilities.encryption.Encryption;
import smartmon.webtools.encryption.EncryptionService;

@Api(tags = "encryption")
@RestController
@RequestMapping("${smartmon.api.prefix:/api/v2}/encryption")
@Slf4j
public class EncryptionApi {
  @Autowired
  private EncryptionService encryptionService;

  @GetMapping("test")
  public String test() {
    final Encryption encryption = encryptionService.getEncryption();
    final String result = encryption.encryptQuietly("admin");
    log.debug("result: {}", result);
    final String result2 = encryption.decryptQuietly(result);
    log.debug("result2: {}", result2);
    return "ok";
  }
}
