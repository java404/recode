package smartmon.vhe.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.controller.vo.HostInitParamVo;
import smartmon.vhe.service.StorageHostService;
import smartmon.vhe.service.dto.StorageHostDto;
import smartmon.vhe.service.feign.SmartStorFeignClient;

@RestController
@RequestMapping("/api/v2/hosts")
public class HostController {
  @Autowired
  private StorageHostService hostService;

  @Autowired
  private SmartStorFeignClient client;

  @PostMapping("init")
  public SmartMonResponse<String> initHost(@RequestBody List<HostInitParamVo> hostsVo) {
    hostService.init(StorageHostDto.convertToList(hostsVo));
    return SmartMonResponse.OK;
  }
}
