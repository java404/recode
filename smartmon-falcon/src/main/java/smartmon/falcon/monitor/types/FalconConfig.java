package smartmon.falcon.monitor.types;

import java.io.Serializable;
import lombok.Data;

@Data
public class FalconConfig implements Serializable {
  private static final long serialVersionUID = -4784788250554205426L;

  private String name;
  private String value;
}
