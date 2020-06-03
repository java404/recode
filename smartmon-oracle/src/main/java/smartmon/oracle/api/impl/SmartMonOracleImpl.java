package smartmon.oracle.api.impl;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import smartmon.oracle.api.SmartMonOracle;
import smartmon.oracle.misc.OraInventory;
import smartmon.oracle.types.OraInventoryHome;

@Service
public class SmartMonOracleImpl implements SmartMonOracle {
  @Override
  public List<OraInventoryHome> getClustersInfo() {
    final OraInventory oraInventory = OraInventory.loadFromConfig();
    if (oraInventory == null) {
      return Collections.emptyList();
    }
    return oraInventory.parseHomeList();
  }
}
