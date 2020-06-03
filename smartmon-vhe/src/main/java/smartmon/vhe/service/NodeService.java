package smartmon.vhe.service;

import smartmon.vhe.service.dto.NodeDetailDto;

public interface NodeService {

  NodeDetailDto getNodeDetailInfo(String hostUuid);
}
