package smartmon.database.oracle.impl;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.hosts.SmartMonHost;
import smartmon.core.provider.SmartMonCoreProvider;
import smartmon.database.exception.SmartMonExceptionInvalidHostUuid;
import smartmon.database.oracle.SmartMonOracleService;
import smartmon.oracle.provider.SmartMonOracleProvider;
import smartmon.oracle.provider.SmartMonOracleRemote;
import smartmon.oracle.types.OraInventoryHome;
import smartmon.utilities.misc.TargetHost;

@Slf4j
@Service
public class SmartMonOracleServiceImpl implements SmartMonOracleService {
  @Autowired
  private SmartMonOracleProvider oracleProvider;

  @DubboReference(version = "${dubbo.service.version}", check = false, lazy = true)
  private SmartMonCoreProvider smartMonCoreProvider;

  private SmartMonOracleRemote getOracleRemote(String hostUuid) {
    final SmartMonHost smartMonHost = smartMonCoreProvider.findSmartMonHostByUuid(hostUuid);
    if (smartMonHost == null) {
      log.error("Cannot find host {}", hostUuid);
      throw new SmartMonExceptionInvalidHostUuid();
    }
    final TargetHost targetHost = TargetHost.builder(smartMonHost.getManageIp(), 1989).build();
    return oracleProvider.getOracleRemote(targetHost);
  }

  @Override
  public List<OraInventoryHome> getClusterInfo(String hostUuid) {
    return getOracleRemote(hostUuid).listClusterInfo();
  }
}
