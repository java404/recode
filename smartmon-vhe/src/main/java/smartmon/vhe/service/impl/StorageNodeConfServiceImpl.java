package smartmon.vhe.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.BeanConverter;
import smartmon.utilities.misc.JsonConverter;
import smartmon.vhe.config.Constant;
import smartmon.vhe.controller.vo.NodeDmPathVo;
import smartmon.vhe.controller.vo.NodeDmVo;
import smartmon.vhe.service.StorageNodeConfService;
import smartmon.vhe.service.dto.NodeConfDto;
import smartmon.vhe.service.dto.NodeConfFileSystemDto;
import smartmon.vhe.service.dto.NodeConfSystemDiskDto;
import smartmon.vhe.service.dto.NodeConfSystemNicDto;
import smartmon.vhe.service.dto.NodeConfSystemOsDto;
import smartmon.vhe.service.dto.NodeDmDto;
import smartmon.vhe.service.dto.NodeDmPathDto;
import smartmon.vhe.service.feign.SmartmonFalconFeignClient;

@Service
@Slf4j
public class StorageNodeConfServiceImpl implements StorageNodeConfService {

  @Autowired
  private SmartmonFalconFeignClient falconFeignClient;

  @Override
  public List<NodeDmVo> getDmInfos(String hostGuid) {
    SmartMonResponse<List<NodeConfDto>> response = falconFeignClient.getNodeConfs(hostGuid, Constant.NODE_CONF_DM);
    if (response == null || response.getErrno() != 0 || CollectionUtils.isEmpty(response.getContent())) {
      return Lists.newArrayList();
    }
    List<NodeDmVo> result = new ArrayList<>();
    List<NodeConfDto> content = response.getContent();
    for (NodeConfDto dto : content) {
      JsonNode dmTree = dto.getData();
      if (dmTree == null) {
        continue;
      }
      for (Iterator<Map.Entry<String, JsonNode>> it = dmTree.fields(); it.hasNext(); ) {
        Map.Entry<String, JsonNode> entry = it.next();
        JsonNode value = entry.getValue();
        if (value == null) {
          continue;
        }
        NodeDmDto nodeConfDto = JsonConverter.readValueQuietly(value.toString(), NodeDmDto.class);
        if (nodeConfDto == null) {
          continue;
        }
        NodeDmVo dmVo = BeanConverter.copy(nodeConfDto, NodeDmVo.class);
        if (dmVo == null) {
          continue;
        }
        getDmPaths(nodeConfDto, dmVo);
        result.add(dmVo);
      }
    }
    return result;
  }

  private void getDmPaths(NodeDmDto nodeConfDto, NodeDmVo dmVo) {
    JsonNode paths = nodeConfDto.getPaths();
    List<NodeDmPathVo> pathVos = new ArrayList<>();
    for (Iterator<Map.Entry<String, JsonNode>> i = paths.fields(); i.hasNext(); ) {
      try {
        Map.Entry<String, JsonNode> pathEntry = i.next();
        JsonNode pathValue = pathEntry.getValue();
        if (pathValue == null) {
          continue;
        }
        NodeDmPathDto dmPath = JsonConverter.readValueQuietly(pathValue.toString(), NodeDmPathDto.class);
        if (dmPath == null) {
          continue;
        }
        NodeDmPathVo pathVo = BeanConverter.copy(dmPath, NodeDmPathVo.class);
        if (pathVo == null) {
          continue;
        }
        pathVo.setHctl(pathEntry.getKey());
        pathVo.setMajorMinor(dmPath.getDevno());
        pathVos.add(pathVo);
      } catch (Exception e) {
        log.warn("Get dm path info failed", e);
      }
    }
    dmVo.setDmPaths(pathVos);
  }

  public List<NodeConfFileSystemDto> getFileSystem(JsonNode contentData) {
    List<NodeConfFileSystemDto> fileSystems = new ArrayList<>();
    for (Iterator<JsonNode> it = contentData.elements(); it.hasNext();) {
      JsonNode node = it.next();
      try {
        NodeConfFileSystemDto fileSystemDto = JsonConverter.treeToValue(node, NodeConfFileSystemDto.class);
        if (fileSystemDto != null) {
          fileSystems.add(fileSystemDto);
        }
      } catch (Exception e) {
        log.warn("Parse file system failed", e);
      }
    }
    return fileSystems;
  }

  public List<NodeConfSystemNicDto> getNicInfo(JsonNode contentData) {
    List<NodeConfSystemNicDto> nics = new ArrayList<>();
    try {
      NodeConfSystemOsDto osDto = JsonConverter.treeToValue(contentData, NodeConfSystemOsDto.class);
      if (osDto == null || CollectionUtils.isEmpty(osDto.getNic())) {
        return nics;
      }
      nics = osDto.getNic();
    } catch (Exception e) {
      log.warn("Parse nic info failed", e);
    }
    return nics;
  }

  public NodeConfSystemDiskDto getSystemDisk(JsonNode contentData) {
    try {
      return JsonConverter.treeToValue(contentData, NodeConfSystemDiskDto.class);
    } catch (Exception e) {
      log.warn("Parse system disk info failed", e);
    }
    return null;
  }
}
