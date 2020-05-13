package smartmon.falcon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.falcon.controller.vo.HostGroupModifyVo;
import smartmon.falcon.host.HostGroupService;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "hostGroups")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/hostGroups")
@Slf4j
@RestController
public class HostGroupController {
  @Resource
  private HostGroupService hostGroupService;

  /**
   * Get Host Group List.
   */
  @ApiOperation("Get Host Group List")
  @GetMapping
  public SmartMonResponse getHostGroupList(@RequestParam(value = "groupRegex",
      required = false) String groupRegex) {
    return new SmartMonResponse<>(hostGroupService.getHostGroups(groupRegex));
  }

  /**
   * Get hosts from group.
   */
  @ApiOperation("Get hosts from group.")
  @PostMapping("group/{groupId}")
  public SmartMonResponse getHostsByGroup(@PathVariable Integer groupId) {
    return new SmartMonResponse<>(hostGroupService.getHostsByGroupId(groupId));
  }


  /**
   * Add host List.
   */
  @ApiOperation("Add List.")
  @PostMapping
  public SmartMonResponse addHostGroupList(@RequestBody HostGroupModifyVo hostGroupAddVo) {
    return null;
  }

  /**
   * Delete host from group.
   */
  @ApiOperation("Delete host from group.")
  @DeleteMapping
  public SmartMonResponse deleteHostGroupList(@RequestBody HostGroupModifyVo hostGroupAddVo) {
    return null;
  }
}
