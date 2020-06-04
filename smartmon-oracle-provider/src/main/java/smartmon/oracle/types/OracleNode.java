package smartmon.oracle.types;

import java.io.Serializable;
import lombok.Data;

@Data
public class OracleNode implements Serializable {
  private static final long serialVersionUID = -8841788581444556908L;

  private String name;
  private int number;
  private String vip;
}
