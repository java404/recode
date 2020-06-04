package smartmon.database.oracle;

import java.util.List;
import smartmon.oracle.types.OraInventoryHome;

public interface SmartMonOracleService {
  List<OraInventoryHome> getClusterInfo(String hostId);
}
