package smartmon.smartstor.infra.cache;

import com.google.common.collect.Lists;

import java.util.List;

import org.springframework.stereotype.Component;
import smartmon.smartstor.domain.gateway.repository.DiskRepository;
import smartmon.smartstor.domain.model.Disk;

@Component
public class DiskRepositoryInCache implements DiskRepository {
  //TODO: save and get data from cache data-source
  @Override
  public List<Disk> getDisks() {
    return Lists.newArrayList();
  }
}
