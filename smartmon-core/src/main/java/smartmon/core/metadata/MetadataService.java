package smartmon.core.metadata;

import java.util.List;
import smartmon.core.metadata.types.Metadata;

public interface MetadataService {
  List<Metadata> getAll();

  void put(List<Metadata> items);
}
