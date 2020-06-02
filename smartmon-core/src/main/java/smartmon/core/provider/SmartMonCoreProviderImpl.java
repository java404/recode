package smartmon.core.provider;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import smartmon.core.store.SmartMonStoreFile;
import smartmon.core.store.SmartMonStoreInputFile;
import smartmon.core.store.SmartMonStoreService;

@DubboService(interfaceClass = SmartMonCoreProvider.class, version = "${dubbo.service.version}")
public class SmartMonCoreProviderImpl implements SmartMonCoreProvider {
  @Autowired
  private SmartMonStoreService storeService;

  @Override
  public SmartMonStoreFile put(SmartMonStoreInputFile localFile) {
    return storeService.put(localFile);
  }

  @Override
  public SmartMonStoreFile findById(long fileId) {
    return storeService.findById(fileId);
  }
}
