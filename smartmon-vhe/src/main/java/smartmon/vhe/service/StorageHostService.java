package smartmon.vhe.service;

import java.util.List;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.service.dto.StorageHostDto;

public interface StorageHostService {
  Boolean init(List<StorageHostDto> hostDtos);
}
