package smartmon.webtools.auth.impl;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smartmon.webtools.auth.SmartMonAuthService;
import smartmon.webtools.auth.SmartMonToken;

@Service
public class SmartMonAuthServiceImpl implements SmartMonAuthService {
  @Value("${smartmon.auth.secret:Zelda}")
  private String secret;
  private SmartMonToken smartMonToken;

  @PostConstruct
  void init() {
    smartMonToken = new SmartMonToken(secret);
  }

  @Override
  public SmartMonToken getSmartMonToke() {
    return smartMonToken;
  }
}
