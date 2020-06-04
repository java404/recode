package smartmon.webtools.general;

import java.io.Serializable;
import lombok.Data;

@Data
public class SmartMonAuthority implements Serializable {
  private static final long serialVersionUID = 6644797229219904983L;

  private Long authorityId;
  private String name;
}
