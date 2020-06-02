package smartmon.webtools.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Date;
import java.util.List;
import smartmon.utilities.misc.StringItems;

public class SmartMonToken {
  private final Jws<Claims> claimsJws;

  SmartMonToken(Jws<Claims> claimsJws, long expirationTime) {
    this.claimsJws = claimsJws;
  }

  public Boolean validateToken() {
    return !isTokenExpired();
  }

  private Boolean isTokenExpired() {
    final Date expiration = claimsJws.getBody().getExpiration();
    return expiration.before(new Date());
  }

  public List<String> getRoles() {
    return StringItems.parse(claimsJws.getBody().get("roles", String.class));
  }
}
