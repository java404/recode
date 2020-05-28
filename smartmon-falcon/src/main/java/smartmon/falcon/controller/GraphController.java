package smartmon.falcon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.falcon.controller.vo.GeneralMonitorGraphQueryVo;
import smartmon.falcon.controller.vo.GraphHistoryQueryVo;

import smartmon.falcon.graph.GraphService;
import smartmon.falcon.graph.command.GeneralMonitorGraphQueryCommand;
import smartmon.falcon.graph.command.GraphHistoryQueryCommand;
import smartmon.falcon.graph.command.LastGraphValueCompareQueryCommand;
import smartmon.falcon.graph.model.EndpointCounter;
import smartmon.falcon.graph.model.GraphRecord;

import smartmon.falcon.web.result.LastGraphValueResult;
import smartmon.falcon.web.vo.LastGraphValueCompareQueryVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.BeanConverter;


@Api(tags = "graphs")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/graphs")
@RestController
public class GraphController {
  @Autowired
  private GraphService graphService;

  /**
   * Get Graph History.
   */
  @ApiOperation("Get Graph History")
  @PostMapping
  public SmartMonResponse<List<GraphRecord>> getGraphHistory(
    @RequestBody GraphHistoryQueryVo graphHistoryQueryVo) {
    final GraphHistoryQueryCommand queryCommand = BeanConverter.copy(graphHistoryQueryVo,
      GraphHistoryQueryCommand.class);
    return new SmartMonResponse<>(graphService.getGraphHistory(queryCommand));
  }

  /**
   * Get Endpoint Counter List.
   */
  @ApiOperation("Get Endpoint Counter List")
  @PostMapping("endpoint-counter/{id}")
  public SmartMonResponse<List<EndpointCounter>> getEndpointCounterList(
    @PathVariable("id") Integer endpointId) {
    return new SmartMonResponse<>(graphService.getEndpointCounters(endpointId));
  }

  /**
   * General Monitor Graph History.
   */
  @ApiOperation("General Monitor Graph History")
  @PostMapping("general")
  public SmartMonResponse<List<GraphRecord>> generalGraphHistory(@RequestBody GeneralMonitorGraphQueryVo graphQueryVo) {
    final GeneralMonitorGraphQueryCommand queryCommand = BeanConverter.copy(graphQueryVo,
      GeneralMonitorGraphQueryCommand.class);
    return new SmartMonResponse<>(graphService.generalGraphHistory(queryCommand));
  }

  /**
   * Last Graph Value Compare Threshold Value.
   */
  @ApiOperation("Last Graph Value Compare Threshold Value")
  @PostMapping("threshold/compare")
  public SmartMonResponse<List<LastGraphValueResult>> compareThresholdValue(
    @RequestBody LastGraphValueCompareQueryVo graphQueryVo) {
    final LastGraphValueCompareQueryCommand queryCommand = BeanConverter.copy(graphQueryVo,
      LastGraphValueCompareQueryCommand.class);
    return new SmartMonResponse<>(graphService.compareThresholdResult(queryCommand));
  }
}
