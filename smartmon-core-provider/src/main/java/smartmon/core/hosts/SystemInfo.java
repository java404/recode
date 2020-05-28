package smartmon.core.hosts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemInfo {
  private String systemVendor;
  private String system;
  private String architecture;
  private String osFamily;
  private String distribution;
  private String kernel;
}
