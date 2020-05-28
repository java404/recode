package smartmon.falcon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import smartmon.falcon.template.Template;
import smartmon.falcon.template.TemplateService;
import smartmon.falcon.user.Team;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.webtools.page.SmartMonPageParams;
import smartmon.webtools.page.SmartMonPageResponseBuilder;

@Api(tags = "templates")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/templates")
@Slf4j
@RestController
public class TemplateController {
  @Autowired
  private TemplateService templateService;
  @Autowired
  private TaskManagerService taskManagerService;

  /**
   * Get template List.
   */
  @ApiOperation("Get template List")
  @SmartMonPageParams
  @GetMapping
  public SmartMonResponse<Page<Template>> getTemplateList(ServerHttpRequest request) {
    return new SmartMonPageResponseBuilder<>(templateService.listTemplate(),
      request, "id").build();
  }

  /**
   * Get template list by group id.
   */
  @ApiOperation("Get template list by group id.")
  @GetMapping("group/{id}")
  public SmartMonResponse<List<Template>> getTemplateByGroupId(@PathVariable Integer id) {
    return new SmartMonResponse<>(templateService.getTemplatesByGroupId(id));
  }

  /**
   * Get strategy list by template id.
   */
  @ApiOperation("Get strategy list by template id.")
  @SmartMonPageParams
  @GetMapping("{id}/strategy")
  public SmartMonResponse<Page<Strategy>> getStrategiesByTemplateId(
      @PathVariable Integer id, ServerHttpRequest request) {
    return new SmartMonPageResponseBuilder<>(templateService
      .getStrategiesByTemplateId(id), request, "id").build();
  }

  /**
   * Get team list by template id.
   */
  @ApiOperation("Get team list by template id.")
  @GetMapping("{id}/team")
  public SmartMonResponse<List<Team>> getTeamsByTemplateId(@PathVariable Integer id) {
    return new SmartMonResponse<>(templateService.getTeamsByTemplateId(id));
  }

  /**
   * Add team into template.
   */
  @ApiOperation("Add team into template.")
  @PatchMapping("team/bind")
  public SmartMonResponse<TaskGroupVo> bindTeamAndTemplate(
      @RequestBody TemplateTeamAddVo templateTeamAddVo) {
    final TaskDescription description = new TaskDescriptionBuilder()
        .withAction(TaskAct.ACT_PATCH).withResource(TaskRes.RES_FALCON_TEMPLATE)
        .withParameters(templateTeamAddVo)
        .withStep("Bind", "Bind Team", () -> templateService.bindTeamAndTemplate(templateTeamAddVo
        .getTemplateId(), templateTeamAddVo.getTeamName()))
        .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("invokeTask", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());

  }

  /**
   * Delete team from template.
   */
  @ApiOperation("Delete team from template.")
  @PatchMapping("team/unbind")
  public SmartMonResponse<TaskGroupVo> unbindTeamAndTemplate(@RequestBody TemplateTeamAddVo templateTeamAddVo) {
    final TaskDescription description = new TaskDescriptionBuilder()
        .withAction(TaskAct.ACT_PATCH).withResource(TaskRes.RES_FALCON_TEMPLATE)
        .withParameters(templateTeamAddVo)
        .withStep("Unbind", "Unbind Team", () -> templateService
        .unbindTeamAndTemplate(templateTeamAddVo.getTemplateId(),
        templateTeamAddVo.getTeamName()))
        .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("invokeTask", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }
}
