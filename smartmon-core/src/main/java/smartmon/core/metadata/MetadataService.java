package smartmon.core.metadata;

import java.util.List;
import smartmon.core.metadata.types.Metadata;

public interface MetadataService {
  List<Metadata> findItems(String key);

  Metadata findItem(String name);

  void put(List<Metadata> items);
}
