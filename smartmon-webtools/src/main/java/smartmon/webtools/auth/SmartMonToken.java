package smartmon.webtools.auth;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Map;
import smartmon.utilities.misc.SmartMonTime;

public class SmartMonToken {
  private static final long DEFAULT_EXPIRATION_SECONDS = 60 * 60 * 30;
  private final String secret;
  private final long expirationSeconds;

  public SmartMonToken(String secret, long expirationSeconds) {
    this.secret = secret;
    this.expirationSeconds = expirationSeconds;
  }

  public SmartMonToken(String secret) {
    this(secret, DEFAULT_EXPIRATION_SECONDS);
  }

  public String generateToken(String subject, Map<String, Object> claims) {
    return Jwts.builder()
      .setClaims(claims).setSubject(subject)
      .setIssuedAt(SmartMonTime.now())
      .setExpiration(SmartMonTime.expirationSeconds(expirationSeconds))
      .signWith(SignatureAlgorithm.HS512, secret)
      .compact();
  }

  public Claims parseToken(String token) {
    return Jwts.parser()
      .setSigningKey(secret).parseClaimsJws(Strings.nullToEmpty(token))
      .getBody();
  }
}
