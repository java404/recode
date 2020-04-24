package smartmon.smartstor.interfaces.web.representation.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.interfaces.web.representation.dto.StorageHostDto;

public class StorageHostDtoAssembler {
  public static List<StorageHostDto> toDtoList(List<StorageHost> storageHosts) {
    return storageHosts.stream().map(StorageHostDtoAssembler::toDto).collect(Collectors.toList());
  }

  private static StorageHostDto toDto(StorageHost storageHost) {
    StorageHostDto storageHostDto = new StorageHostDto();
    BeanUtils.copyProperties(storageHost, storageHostDto);
    return storageHostDto;
  }
}
