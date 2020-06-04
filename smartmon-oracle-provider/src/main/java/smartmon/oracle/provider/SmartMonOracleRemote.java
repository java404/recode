package smartmon.oracle.provider;

import feign.RequestLine;
import java.util.List;
import smartmon.oracle.types.OraInventoryHome;

public interface SmartMonOracleRemote {
  @RequestLine("GET /clusters")
  List<OraInventoryHome> listClusterInfo();
}
