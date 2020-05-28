package smartmon.smartstor.app.command;

import lombok.Data;

@Data
public class GroupNodeAddCommand {
  private String serviceIp;
  private String nodeName;
  private String groupName;
}
