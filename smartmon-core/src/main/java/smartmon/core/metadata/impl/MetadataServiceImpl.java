package smartmon.core.metadata.impl;

import com.google.common.base.Strings;
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
  public List<Metadata> findItems(String key) {
    if (Strings.isNullOrEmpty(key)) {
      return metadataMapper.findAll();
    } else {
      return metadataMapper.findItems("%" + Strings.nullToEmpty(key) + "%");
    }
  }

  @Override
  public Metadata findItem(String name) {
    return metadataMapper.findItem(name);
  }

  @Override
  public void put(List<Metadata> items) {
    for (final Metadata item : items) {
      log.debug("Adding metadata: {}", Strings.nullToEmpty(item.getName()));
      if (Strings.isNullOrEmpty(item.getName())) {
        continue;
      }
      try {
        metadataMapper.put(item);
      } catch (Exception err) {
        log.error("Cannot put metadata {}", item.getName(), err);
      }
    }
  }
}
