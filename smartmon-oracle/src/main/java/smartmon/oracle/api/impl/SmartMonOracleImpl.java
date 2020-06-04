package smartmon.oracle.api.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import smartmon.oracle.api.SmartMonOracle;
import smartmon.oracle.exec.Olsnodes;
import smartmon.oracle.exec.Sqlplus;
import smartmon.oracle.misc.OraInventory;
import smartmon.oracle.types.OraInventoryInfo;
import smartmon.oracle.types.OracleClusterInfo;

@Service
public class SmartMonOracleImpl implements SmartMonOracle {
  private List<OraInventoryInfo> parseInventoryInfo() {
    final OraInventory oraInventory = OraInventory.loadFromConfig();
    if (oraInventory == null) {
      return Collections.emptyList();
    }
    return oraInventory.parseHomeList().stream().map(item -> {
      final OraInventoryInfo inventoryInfo = new OraInventoryInfo();
      inventoryInfo.setHome(item);
      final Sqlplus sqlplus = new Sqlplus(item.getLoc());
      inventoryInfo.setOracleVersion(sqlplus.getVersion());
      return inventoryInfo;
    }).collect(Collectors.toList());
  }

  @Override
  public OracleClusterInfo getClusterInfo() {
    final OracleClusterInfo clusterInfo = new OracleClusterInfo();
    clusterInfo.setNodes(Olsnodes.getInstance().listNodes());
    clusterInfo.setInventory(parseInventoryInfo());
    return clusterInfo;
  }
}
