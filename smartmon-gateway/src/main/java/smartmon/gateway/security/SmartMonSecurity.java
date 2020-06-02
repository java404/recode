package smartmon.gateway.security;

import smartmon.webtools.jwt.SmartMonJsonWebToken;

public interface SmartMonSecurity {
  SmartMonJsonWebToken getJsonWebToken();
}
