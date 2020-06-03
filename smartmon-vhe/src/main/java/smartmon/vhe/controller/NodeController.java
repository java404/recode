package smartmon.vhe.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.BeanConverter;
import smartmon.vhe.controller.vo.NodeFileSystemVo;
import smartmon.vhe.controller.vo.NodeNicInfoVo;
import smartmon.vhe.controller.vo.NodeDetailVo;
import smartmon.vhe.controller.vo.NodeSystemDiskVo;
import smartmon.vhe.service.NodeService;
import smartmon.vhe.service.dto.NodeDetailDto;

@Api(tags = "nodes")
@RestController
@RequestMapping("${smartmon.api.prefix:/vhe/api/v2}/nodes")
@Slf4j
public class NodeController {

  @Autowired
  private NodeService nodeService;

  @GetMapping("info")
  public SmartMonResponse<NodeDetailVo> getNodeInfo(@RequestParam String hostUuid) {
    NodeDetailDto detailDto = nodeService.getNodeDetailInfo(hostUuid);
    NodeDetailVo vo = BeanConverter.copy(detailDto, NodeDetailVo.class);
    if (detailDto != null && vo != null) {
      vo.setSystemDiskInfo(BeanConverter.copy(detailDto.getSystemDisk(), NodeSystemDiskVo.class));
      vo.setFileSystems(BeanConverter.copy(detailDto.getFileSystem(), NodeFileSystemVo.class));
      vo.setNicInfos(BeanConverter.copy(detailDto.getNics(), NodeNicInfoVo.class));
    }
    return new SmartMonResponse<>(vo);
  }
}
