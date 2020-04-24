package smartmon.smartstor.interfaces.web.representation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.domain.gateway.repository.DiskRepository;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.interfaces.web.representation.assembler.DiskDtoAssembler;
import smartmon.smartstor.interfaces.web.representation.dto.DiskDto;

@Service
public class DiskRepresentationService {
  @Autowired
  private DiskRepository diskRepository;

  public List<DiskDto> getDisks() {
    List<Disk> disks = diskRepository.getDisks();
    return DiskDtoAssembler.toDtos(disks);
  }
}
