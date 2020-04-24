package smartmon.core.metadata.impl;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.metadata.MetadataService;
import smartmon.core.metadata.types.Metadata;

@Service
@Slf4j
public class MetadataServiceImpl implements MetadataService {
  @Autowired
  private MetadataMapper metadataMapper;

  @Override
  public List<Metadata> getAll() {
    return metadataMapper.findAll();
  }

  @Override
  public void put(List<Metadata> items) {

  }
}
