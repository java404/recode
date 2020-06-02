package smartmon.core.store;

import java.util.List;
import java.util.Set;

public interface SmartMonStoreService {
  SmartMonStoreFile put(SmartMonStoreInputFile localFile);

  List<SmartMonStoreFile> findAll();

  List<SmartMonStoreFile> remove(Set<Long> fileIds);

  SmartMonStoreFile findById(long fileId);
}
