package smartmon.core.provider;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import smartmon.core.hosts.HostsService;
import smartmon.core.hosts.SmartMonHost;
import smartmon.core.store.SmartMonStoreFile;
import smartmon.core.store.SmartMonStoreInputFile;
import smartmon.core.store.SmartMonStoreService;
import smartmon.utilities.misc.BeanConverter;

import java.util.ArrayList;
import java.util.List;

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
    final List<smartmon.core.hosts.types.SmartMonHost> smartMonHosts = hostsService.getAll();
    List<SmartMonHost> smartMonHostList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(smartMonHosts)) {
      for (smartmon.core.hosts.types.SmartMonHost smartMonHost : smartMonHosts) {
        SmartMonHost host = new SmartMonHost();
        host.setHostUuid(smartMonHost.getHostUuid());
        host.setManageIp(smartMonHost.getManageIp());
        smartMonHostList.add(host);
      }
    }
    return smartMonHostList;
  }
}
