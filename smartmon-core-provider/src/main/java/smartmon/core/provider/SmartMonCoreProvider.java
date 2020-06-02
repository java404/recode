package smartmon.core.provider;

import smartmon.core.store.SmartMonStoreFile;
import smartmon.core.store.SmartMonStoreInputFile;

public interface SmartMonCoreProvider {
  SmartMonStoreFile put(SmartMonStoreInputFile localFile);

  SmartMonStoreFile findById(long fileId);
}
