package smartmon.oracle.api;

import java.util.List;
import smartmon.oracle.types.OraInventoryHome;

public interface SmartMonOracle {
  List<OraInventoryHome> getClustersInfo();
}
