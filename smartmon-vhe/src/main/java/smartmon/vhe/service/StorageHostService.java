package smartmon.vhe.service;

import java.util.List;
import smartmon.vhe.service.dto.VheStorageHostDto;
import smartmon.vhe.service.dto.VheStorageHostInitDto;

public interface StorageHostService {
  Boolean init(List<VheStorageHostInitDto> hostDtos);

  List<VheStorageHostDto> listAll();
}
