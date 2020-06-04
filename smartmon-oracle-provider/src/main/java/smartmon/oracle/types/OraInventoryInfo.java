package smartmon.oracle.types;

import lombok.Data;

@Data
public class OraInventoryInfo {
  private OraInventoryHome home;
  private String oracleVersion;
}
