package smartmon.core.hosts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmartMonHostInfo {
  private BasicInfo basicInfo;
  private SystemInfo systemInfo;
  private HardwareInfo hardwareInfo;
  private NetworkInfo networkInfo;
}
