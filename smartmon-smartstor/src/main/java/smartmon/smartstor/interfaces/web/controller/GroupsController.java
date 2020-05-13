package smartmon.smartstor.interfaces.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.smartstor.interfaces.web.representation.GroupsRepresentationService;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "groups")
@RequestMapping("${smartmon.api.prefix:/smartstor/api/v2}/groups")
@RestController
public class GroupsController {
  @Autowired
  private GroupsRepresentationService groupsRepresentationService;

  @ApiOperation("Get groups")
  @GetMapping
  public SmartMonResponse getGroups(@RequestParam(value = "ServiceIp", required = false) String serviceIp) {
    return new SmartMonResponse<>(groupsRepresentationService.getGroups(serviceIp));
  }



}
