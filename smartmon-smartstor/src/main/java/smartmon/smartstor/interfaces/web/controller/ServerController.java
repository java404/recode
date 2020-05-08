package smartmon.smartstor.interfaces.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.smartstor.interfaces.web.representation.ServerRepresentationService;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "smartstor server")
@RestController
@RequestMapping("${smartmon.api.prefix:/smartstor/api/v2}/server")
public class ServerController {
  @Autowired
  private ServerRepresentationService serverRepresentationService;

  @ApiOperation("Init info")
  @GetMapping("init-info")
  public SmartMonResponse getInitInfo() {
    return new SmartMonResponse<>(serverRepresentationService.getInitInfo());
  }
}
