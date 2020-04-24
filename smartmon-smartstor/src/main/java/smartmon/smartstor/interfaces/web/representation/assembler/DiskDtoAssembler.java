package smartmon.smartstor.interfaces.web.representation.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.interfaces.web.representation.dto.DiskDto;

public class DiskDtoAssembler {
  public static List<DiskDto> toDtos(List<Disk> disks) {
    return disks.stream().map(DiskDtoAssembler::toDto).collect(Collectors.toList());
  }

  private static DiskDto toDto(Disk disk) {
    DiskDto diskDto = new DiskDto();
    BeanUtils.copyProperties(disk, diskDto);
    return diskDto;
  }
}
