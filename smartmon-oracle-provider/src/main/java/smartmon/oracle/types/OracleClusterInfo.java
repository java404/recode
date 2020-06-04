package smartmon.oracle.types;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class OracleClusterInfo implements Serializable {
  private static final long serialVersionUID = 4278621863866305928L;

  private List<OraInventoryInfo> inventory;
  private List<OracleNode> nodes;
}
