package smartmon.core.provider;

import smartmon.core.hosts.SmartMonHost;
import smartmon.core.store.SmartMonStoreFile;
import smartmon.core.store.SmartMonStoreInputFile;

import java.util.List;

public interface SmartMonCoreProvider {
  SmartMonStoreFile put(SmartMonStoreInputFile localFile);

  SmartMonStoreFile findById(long fileId);

  List<SmartMonHost> getSmartMonHosts();
}
