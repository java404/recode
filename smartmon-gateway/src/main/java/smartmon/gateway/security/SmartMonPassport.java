package smartmon.gateway.security;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.gateway.security.types.AuthRequest;
import smartmon.gateway.security.types.AuthResponse;

import smartmon.utilities.general.SmartMonResponse;
import smartmon.webtools.jwt.SmartMonJsonWebToken;
import smartmon.webtools.jwt.SmartMonUser;

@Api(tags = "passport")
@RestController
@RequestMapping("${smartmon.prefix:/gateway/api/v2}/passport")
public class SmartMonPassport {
  @Autowired
  private SmartMonSecurity smartMonSecurity;

  @ApiOperation("User Login")
  @PostMapping("/login")
  public SmartMonResponse<?> login(@RequestBody @Valid AuthRequest authRequest) {
    final SmartMonJsonWebToken smartMonJsonWebToken = smartMonSecurity.getJsonWebToken();

    final SmartMonUser smartMonUser = new SmartMonUser();
    smartMonUser.setUsername("admin");
    smartMonUser.getRoles().add("USER");

    final String token = smartMonJsonWebToken.generate(smartMonUser);
    final AuthResponse authResponse = new AuthResponse();
    authResponse.setToken(token);
    return new SmartMonResponse<>(authResponse);
  }
}
