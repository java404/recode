package smartmon.falcon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.falcon.host.Endpoint;
import smartmon.falcon.host.EndpointService;
import smartmon.utilities.general.SmartMonResponse;


@Api(tags = "endpoints")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/endpoints")
@RestController
public class EndpointController {
  @Autowired
  private EndpointService endpointService;

  /**
   * Get Endpoint List.
   */
  @ApiOperation("Get Endpoint List")
  @GetMapping
  public SmartMonResponse<List<Endpoint>> getEndpointList(
    @RequestParam(value = "endpoint-regex", required = false) String regex) {
    return new SmartMonResponse<>(endpointService.getEndpoints(regex));
  }
}
