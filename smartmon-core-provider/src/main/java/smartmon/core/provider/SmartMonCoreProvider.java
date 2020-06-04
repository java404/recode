package smartmon.core.provider;

import java.util.List;

import smartmon.core.hosts.SmartMonHost;
import smartmon.core.store.SmartMonStoreFile;
import smartmon.core.store.SmartMonStoreInputFile;

public interface SmartMonCoreProvider {
  SmartMonStoreFile put(SmartMonStoreInputFile localFile);

  SmartMonStoreFile findById(long fileId);

  List<SmartMonHost> getSmartMonHosts();

  SmartMonHost findSmartMonHostByUuid(String uuid);
}
