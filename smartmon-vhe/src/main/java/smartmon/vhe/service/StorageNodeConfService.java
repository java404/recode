package smartmon.vhe.service;

import java.util.List;

import smartmon.vhe.controller.vo.NodeDmVo;


public interface StorageNodeConfService {
  List<NodeDmVo> getDmInfos(String hostname);
}
