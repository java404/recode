package smartmon.vhe.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.controller.vo.NodeDmVo;
import smartmon.vhe.service.StorageNodeConfService;

@Api(tags = "Storage Node Conf")
@RequestMapping("${smartmon.api.prefix:/vhe/api/v2}/node-conf")
@RestController
public class StorageNodeConfController {
  @Autowired
  private StorageNodeConfService storageNodeConfService;

  @ApiOperation("Get dm infos")
  @GetMapping("dm")
  public SmartMonResponse<List<NodeDmVo>> getDisks(@RequestParam("hostGuid") String hostGuid) {
    return new SmartMonResponse<>(storageNodeConfService.getDmInfos(hostGuid));
  }

}
