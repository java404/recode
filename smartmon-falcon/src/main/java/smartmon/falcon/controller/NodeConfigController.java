package smartmon.falcon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.falcon.controller.vo.NodeConfigCreateVo;
import smartmon.falcon.entity.NodeConfigEntity;
import smartmon.falcon.service.NodeConfigService;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.JsonConverter;

@Api(tags = "node_configs")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/node_configs")
@RestController
public class NodeConfigController {
  @Autowired
  private NodeConfigService nodeConfigService;

  /**
   * Get node config List.
   */
  @ApiOperation("Get Node Config List")
  @GetMapping
  public SmartMonResponse<List<NodeConfigEntity>> getNodeConfigList(
      @RequestParam(value = "hostname", defaultValue = "") String hostname,
      @RequestParam(value = "name", defaultValue = "") String name) {
    return new SmartMonResponse<>(nodeConfigService.getAll(hostname, name));
  }

  /**
   * Create Node Config.
   */
  @ApiOperation("Create Or Update Node Config")
  @PostMapping
  public SmartMonResponse<NodeConfigEntity> createNodeConfig(@RequestBody NodeConfigCreateVo createVo) {
    String dataStr = JsonConverter.writeValueAsStringQuietly(createVo.getData());
    NodeConfigEntity nodeConfigEntity = nodeConfigService.addNodeConfig(dataStr, createVo.getHostname(),
        createVo.getName());
    return new SmartMonResponse<>(nodeConfigEntity);
  }

  /**
   * Delete Node Config.
   */
  @ApiOperation("Delete Node Config")
  @DeleteMapping("{node-config-id}")
  public SmartMonResponse<String> deleteNodeConfig(@PathVariable("node-config-id") Integer id) {
    nodeConfigService.deleteNodeConfig(id);
    return new SmartMonResponse<>("");
  }
}
