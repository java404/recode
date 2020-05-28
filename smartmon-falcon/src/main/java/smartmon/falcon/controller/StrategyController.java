package smartmon.falcon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.falcon.controller.vo.StrategyUpdateVo;
import smartmon.falcon.remote.types.strategy.FalconStrategyUpdateParam;
import smartmon.falcon.strategy.StrategyService;
import smartmon.falcon.strategy.model.PauseEnum;
import smartmon.falcon.strategy.model.Strategy;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.BeanConverter;

@Api(tags = "strategys")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/strategys")
@Slf4j
@RestController
public class StrategyController {
  @Resource
  private StrategyService strategyService;
  @Autowired
  private TaskManagerService taskManagerService;

  /**
   * Config strategy by strategy id.
   */
  @ApiOperation("Config strategy by strategy id.")
  @PutMapping("{id}")
  public SmartMonResponse<TaskGroupVo> updateStrategyById(@PathVariable Integer id,
                                                          @RequestBody StrategyUpdateVo strategyUpdateVo) {
    final FalconStrategyUpdateParam falconStrategyUpdateParam =
        BeanConverter.copy(strategyUpdateVo, FalconStrategyUpdateParam.class);
    final TaskDescription description = new TaskDescriptionBuilder()
        .withAction(TaskAct.ACT_UPDATE).withResource(TaskRes.RES_FALCON_STRATEGY)
        .withParameters(strategyUpdateVo)
        .withStep("Update", "Update Strategy", () -> strategyService
        .updateStrategyById(id, falconStrategyUpdateParam))
        .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("invokeTask", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());

  }

  /**
   * Enable/Disable strategy by strategy id.
   */
  @ApiOperation("Enable/Disable strategy by strategy id.")
  @PatchMapping("{id}/enable")
  public SmartMonResponse<TaskGroupVo> putStrategyEnableById(@PathVariable Integer id,
                                                             @RequestParam("enable") boolean enable) {
    PauseEnum pauseEnum = PauseEnum.DISABLED;
    if (enable) {
      pauseEnum = PauseEnum.ENABLED;
    }
    PauseEnum finalPauseEnum = pauseEnum;
    final TaskDescription description = new TaskDescriptionBuilder()
        .withAction(TaskAct.ACT_UPDATE).withResource(TaskRes.RES_FALCON_STRATEGY)
        .withParameters(id)
        .withStep("Update", finalPauseEnum.getName() + "Strategy",
          () -> strategyService.setStrategyStatus(id, finalPauseEnum))
        .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("invokeTask", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  /**
   * Get strategy by strategy id.
   */
  @ApiOperation("Get strategy by strategy id.")
  @GetMapping("{id}")
  public SmartMonResponse<Strategy> getStrategyById(@PathVariable Integer id) {
    return new SmartMonResponse<>(strategyService.getStrategyById(id));
  }
}
