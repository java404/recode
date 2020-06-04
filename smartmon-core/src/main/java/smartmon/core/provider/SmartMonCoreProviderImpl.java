package smartmon.core.provider;

import java.util.List;
import org.apache.commons.collections4.ListUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import smartmon.core.hosts.HostsService;
import smartmon.core.hosts.SmartMonHost;
import smartmon.core.store.SmartMonStoreFile;
import smartmon.core.store.SmartMonStoreInputFile;
import smartmon.core.store.SmartMonStoreService;

@DubboService(interfaceClass = SmartMonCoreProvider.class, version = "${dubbo.service.version}")
public class SmartMonCoreProviderImpl implements SmartMonCoreProvider {
  @Autowired
  private SmartMonStoreService storeService;
  @Autowired
  private HostsService hostsService;

  @Override
  public SmartMonStoreFile put(SmartMonStoreInputFile localFile) {
    return storeService.put(localFile);
  }

  @Override
  public SmartMonStoreFile findById(long fileId) {
    return storeService.findById(fileId);
  }

  @Override
  public List<SmartMonHost> getSmartMonHosts() {
    return ListUtils.emptyIfNull(hostsService.getAll());
  }

  @Override
  public SmartMonHost findSmartMonHostByUuid(String uuid) {
    return hostsService.getHostById(uuid);
  }
}
