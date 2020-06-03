package smartmon.oracle.cluster;

import lombok.Data;

@Data
public class OracleNode {
  private String name;
  private int number;
  private String vip;
}
