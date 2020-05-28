package smartmon.smartstor.app.command;

import lombok.Data;

@Data
public class LunAddCommand {
  private String serviceIp;
  private String diskName;
  private String poolName;
  private Integer size;
  private Boolean basedisk;
  private String groupName;
}
