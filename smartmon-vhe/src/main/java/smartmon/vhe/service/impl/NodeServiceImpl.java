package smartmon.vhe.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.InetAddress;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.hosts.SmartMonHostDetailVo;
import smartmon.smartstor.web.dto.StorageHostDto;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.config.Constant;
import smartmon.vhe.service.NodeService;
import smartmon.vhe.service.StorageNodeConfService;
import smartmon.vhe.service.dto.NodeConfDto;
import smartmon.vhe.service.dto.NodeDetailDto;
import smartmon.vhe.service.feign.SmartStorFeignClient;
import smartmon.vhe.service.feign.SmartmonCoreFeignClient;
import smartmon.vhe.service.feign.SmartmonFalconFeignClient;

@Service
@Slf4j
public class NodeServiceImpl implements NodeService {
  @Autowired
  private SmartmonCoreFeignClient smartmonCoreFeignClient;
  @Autowired
  private SmartStorFeignClient smartStorFeignClient;
  @Autowired
  private SmartmonFalconFeignClient falconFeignClient;
  @Autowired
  private StorageNodeConfService nodeConfService;

  @Override
  public NodeDetailDto getNodeDetailInfo(String hostUuid) {
    NodeDetailDto nodeDetailDto = new NodeDetailDto();
    getBaseInfo(hostUuid, nodeDetailDto);
    getSmartstorNodeInfo(hostUuid, nodeDetailDto);
    getNodeConfInfo(hostUuid, nodeDetailDto);
    return nodeDetailDto;
  }

  private void getBaseInfo(String hostUuid, NodeDetailDto nodeDetail) {
    try {
      SmartMonResponse<SmartMonHostDetailVo> res = smartmonCoreFeignClient.getHostInfo(hostUuid);
      if (res == null || res.getErrno() != 0) {
        return;
      }
      SmartMonHostDetailVo hostDetailVo = res.getContent();
      if (hostDetailVo == null) {
        return;
      }
      BeanUtils.copyProperties(hostDetailVo, nodeDetail);
    } catch (Exception e) {
      log.warn("Get base info failed", e);
    }
  }

  private void getSmartstorNodeInfo(String hostUuid, NodeDetailDto nodeDetail) {
    try {
      SmartMonResponse<StorageHostDto> res = smartStorFeignClient.getSingleStorageHost(hostUuid);
      if (res == null || res.getErrno() != 0) {
        return;
      }
      StorageHostDto smartstorHost = res.getContent();
      if (smartstorHost == null) {
        return;
      }
      nodeDetail.setHostType(smartstorHost.getSysModeDesc());
      nodeDetail.setTransMode(smartstorHost.getTransModeDesc());
      nodeDetail.setVersion(smartstorHost.getVersion());
      nodeDetail.setNodeState(accessibleHost(smartstorHost.getListenIp()));
    } catch (Exception e) {
      log.warn("Get smartstor info failed", e);
    }
  }

  private void getNodeConfInfo(String hostGuid, NodeDetailDto nodeDetail) {
    try {
      SmartMonResponse<List<NodeConfDto>> response = falconFeignClient.getNodeConfs(hostGuid, null);
      if (response == null || response.getErrno() != 0 || CollectionUtils.isEmpty(response.getContent())) {
        return;
      }

      List<NodeConfDto> content = response.getContent();
      for (NodeConfDto dto : content) {
        JsonNode tree = dto.getData();
        if (tree == null) {
          continue;
        }
        String name = dto.getName();
        if (Constant.NODE_CONF_DISK_INFO.equalsIgnoreCase(name)) {
          getSystemDisk(tree, nodeDetail);
        } else if (Constant.NODE_CONF_FS.equalsIgnoreCase(name)) {
          getFileSystem(tree, nodeDetail);
        } else if (Constant.NODE_CONF_OS.equalsIgnoreCase(name)) {
          getNicInfo(tree, nodeDetail);
        }
      }
    } catch (Exception e) {
      log.warn("Get node conf info failed", e);
    }
  }

  private void getNicInfo(JsonNode tree, NodeDetailDto nodeDetail) {
    nodeDetail.setNics(nodeConfService.getNicInfo(tree));
  }

  private void getFileSystem(JsonNode tree, NodeDetailDto nodeDetail) {
    nodeDetail.setFileSystem(nodeConfService.getFileSystem(tree));
  }

  private void getSystemDisk(JsonNode tree, NodeDetailDto nodeDetail) {
    nodeDetail.setSystemDisk(nodeConfService.getSystemDisk(tree));
  }


  private boolean accessibleHost(String ip) {
    boolean accessible = false;
    try {
      accessible = InetAddress.getByName(ip)
        .isReachable(3000);
    } catch (Exception e) {
      //
    }
    return accessible;
  }
}
