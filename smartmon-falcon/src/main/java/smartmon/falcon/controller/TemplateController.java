package smartmon.falcon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.falcon.controller.vo.TemplateTeamAddVo;
import smartmon.falcon.strategy.model.Strategy;
import smartmon.falcon.template.TemplateService;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.webtools.page.SmartMonPageResponseBuilder;

@Api(tags = "templates")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/templates")
@Slf4j
@RestController
public class TemplateController {
  @Resource
  private TemplateService templateService;

  /**
   * Get template List.
   */
  @ApiOperation("Get template List.")
  @GetMapping
  public SmartMonResponse getTemplateList() {
    return new SmartMonResponse<>(templateService.listTemplate());
  }

  /**
   * Get template list by group id.
   */
  @ApiOperation("Get template list by group id.")
  @GetMapping("group/{groupId}")
  public SmartMonResponse getTemplateByGroupId(@PathVariable Integer groupId) {
    return new SmartMonResponse<>(templateService.getTemplatesByGroupId(groupId));
  }

  /**
   * Get strategy list by template id.
   */
  @ApiOperation("Get strategy list by template id.")
  @GetMapping("{templateId}/strategy")
  public SmartMonResponse<Page<Strategy>> getStrategiesByTemplateId(
      @PathVariable Integer templateId, ServerHttpRequest request) {
    return new SmartMonPageResponseBuilder<Strategy>(templateService
      .getStrategiesByTemplateId(templateId), request, "id").build();
  }

  /**
   * Get team list by template id.
   */
  @ApiOperation("Get team list by template id.")
  @GetMapping("{templateId}")
  public SmartMonResponse getTeamsByTemplateId(@PathVariable Integer templateId) {
    return new SmartMonResponse<>(templateService.getTeamsByTemplateId(templateId));
  }

  /**
   * Add team into template.
   */
  @ApiOperation("Add team into template.")
  @PatchMapping("team/bind")
  public SmartMonResponse bindTeamAndTemplate(
      @RequestBody TemplateTeamAddVo templateTeamAddVo) {
    return new SmartMonResponse<>(templateService.bindTeamAndTemplate(templateTeamAddVo
        .getTemplateId(), templateTeamAddVo.getTeamName()));
  }

  /**
   * Delete team from template.
   */
  @ApiOperation("Delete team from template.")
  @PatchMapping("team/unbind")
  public SmartMonResponse unbindTeamAndTemplate(@RequestBody TemplateTeamAddVo templateTeamAddVo) {
    return new SmartMonResponse<>(templateService
      .unbindTeamAndTemplate(templateTeamAddVo.getTemplateId(), templateTeamAddVo.getTeamName()));
  }


}
