package smartmon.core.hosts.types;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonitorNetInterfaceVo {
  private String hostUuid;
  private String hostname;
  private List<String> netInterfaces;
  private List<String> monitorNetInterfaces;
}
