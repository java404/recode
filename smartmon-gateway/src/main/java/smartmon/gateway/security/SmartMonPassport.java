package smartmon.gateway.security;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.gateway.security.AuthRequest;
import smartmon.gateway.security.AuthResponse;
import smartmon.gateway.user.UserPrincipal;
import smartmon.gateway.user.UserService;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "passport")
@RestController
@RequestMapping("${smartmon.prefix:/gateway/api/v2}/passport")
public class SmartMonPassport {
  @Autowired
  private UserService userService;

  @ApiOperation("User Login")
  @PostMapping("/login")
  public SmartMonResponse<?> login(@RequestBody @Valid AuthRequest authRequest) {
    final UserPrincipal userPrincipal = userService.findByUsername(authRequest.getUsername());
    final String token = userService.generateToken(userPrincipal);
    final AuthResponse authResponse = new AuthResponse();
    authResponse.setToken(token);
    return new SmartMonResponse<>(authResponse);
  }
}
