package smartmon.webtools.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import smartmon.utilities.misc.StringItems;

public class SmartMonJsonWebToken {
  private final SecretKey key;
  private final long expirationTime;

  public SmartMonJsonWebToken(String secret, long expirationTime) {
    this.expirationTime = expirationTime;
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public SmartMonToken parse(String token) {
    return new SmartMonToken(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token),
      expirationTime);
  }

  public String generate(SmartMonUser user) {
    final Map<String, Object> claims = new HashMap<>();
    claims.put("roles", StringItems.join(user.getRoles()));
    final Date createdDate = new Date();
    final Date expirationDate = new Date(createdDate.getTime() + expirationTime * 1000);
    return Jwts.builder().setClaims(claims).setSubject(user.getUsername())
      .setIssuedAt(createdDate).setExpiration(expirationDate)
      .signWith(key, SignatureAlgorithm.HS512).compact();
  }
}
