package smartmon.core.store;

import java.util.List;

public interface SmartMonStoreService {
  SmartMonStoreFile put(SmartMonStoreInputFile localFile);

  List<SmartMonStoreFile> findAll();

  SmartMonStoreFile findById(long fileId);
}
