package smartmon.smartstor.interfaces.web.representation.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.interfaces.web.representation.dto.DiskDto;

public class DiskDtoAssembler {
  public static CachedData<DiskDto> toDtos(CachedData<Disk> cachedData) {
    if (cachedData == null) {
      return null;
    }
    List<DiskDto> diskDtos = cachedData.getData().stream().map(DiskDtoAssembler::toDto).collect(Collectors.toList());
    return new CachedData<>(diskDtos, cachedData.isExpired(), cachedData.getError());
  }

  private static DiskDto toDto(Disk disk) {
    DiskDto diskDto = new DiskDto();
    BeanUtils.copyProperties(disk, diskDto);
    return diskDto;
  }
}
