package smartmon.gateway.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.utilities.general.SmartMonResponse;

@RestController
@RequestMapping("${smartmon.apiPrefix:/api/v2}/passport")
public class SmartMonPassport {

  @ApiOperation("login")
  @GetMapping
  public SmartMonResponse<String> login() {
    return SmartMonResponse.OK;
  }
}
