package smartmon.gateway.security.impl;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import smartmon.gateway.security.SmartMonSecurity;
import smartmon.webtools.jwt.SmartMonJsonWebToken;

@Service
public class SmartMonSecurityImpl implements SmartMonSecurity {
  private SmartMonJsonWebToken smartMonJsonWebToken;

  private static final String key = "ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength";

  @PostConstruct
  void init() {
    // TODO load config from yaml
    smartMonJsonWebToken = new SmartMonJsonWebToken(key, 60 * 300);
  }

  @Override
  public SmartMonJsonWebToken getJsonWebToken() {
    return smartMonJsonWebToken;
  }
}
