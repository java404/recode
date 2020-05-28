package smartmon.gateway.security.impl;

import org.springframework.stereotype.Service;
import smartmon.gateway.security.AuthenticateService;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {
  @Override
  public void check(String token, String api, String method) {

  }
}
