package smartmon.webtools.auth;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class SmartMonTokenTest {
  @Test
  public void testMakeToken() {
    final SmartMonToken smartMonToken = new SmartMonToken("test");
    final Map<String, Object> claims =  new HashMap<>();
    claims.put("username", "root");
    claims.put("roles", "admin,user");
    final String token = smartMonToken.generateToken("smartmon", claims);
    Assert.assertFalse(Strings.isNullOrEmpty(token));
    final Claims claimsParse = smartMonToken.parseToken(token);
  }
}
