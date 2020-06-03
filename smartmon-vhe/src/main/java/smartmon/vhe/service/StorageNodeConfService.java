package smartmon.vhe.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import smartmon.vhe.controller.vo.NodeDmVo;
import smartmon.vhe.service.dto.NodeConfFileSystemDto;
import smartmon.vhe.service.dto.NodeConfSystemDiskDto;
import smartmon.vhe.service.dto.NodeConfSystemNicDto;


public interface StorageNodeConfService {
  List<NodeDmVo> getDmInfos(String hostname);

  List<NodeConfFileSystemDto> getFileSystem(JsonNode contentData);

  List<NodeConfSystemNicDto> getNicInfo(JsonNode contentData);

  NodeConfSystemDiskDto getSystemDisk(JsonNode contentData);
}
