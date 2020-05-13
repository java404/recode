package smartmon.falcon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
import smartmon.falcon.template.TemplateService;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.BeanConverter;


@Api(tags = "strategys")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/strategys")
@Slf4j
@RestController
public class StrategyController {
  @Resource
  private StrategyService strategyService;

  /**
   * Config strategy by strategy id.
   */
  @ApiOperation("Config strategy by strategy id.")
  @PutMapping("{strategyId}")
  public SmartMonResponse updateStrategyById(@PathVariable Integer strategyId,
                                             @RequestBody StrategyUpdateVo strategyUpdateVo) {
    final FalconStrategyUpdateParam falconStrategyUpdateParam =
        BeanConverter.copy(strategyUpdateVo, FalconStrategyUpdateParam.class);
    return new SmartMonResponse(strategyService
      .updateStrategyById(strategyId, falconStrategyUpdateParam).getSuccessMessage());
  }

  /**
   * Enable/Disable strategy by strategy id.
   */
  @ApiOperation("Enable/Disable strategy by strategy id.")
  @PatchMapping("{strategyId}/enable")
  public SmartMonResponse putStrategyEnableById(@PathVariable Integer strategyId,
                                                @RequestParam(value = "enable",
      required = true) boolean enable) {
    PauseEnum pauseEnum = PauseEnum.DISABLED;
    if (enable) {
      pauseEnum = PauseEnum.ENABLED;
    }
    return new SmartMonResponse(strategyService.setStrategyStatus(strategyId,
        pauseEnum).getSuccessMessage());
  }

  /**
   * Get strategy by strategy id.
   */
  @ApiOperation("Get strategy by strategy id.")
  @GetMapping("{strategyId}")
  public SmartMonResponse getStrategyById(@PathVariable Integer strategyId) {
    return new SmartMonResponse(strategyService.getStrategyById(strategyId));
  }



}
