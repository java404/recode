package smartmon.smartstor.app.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupAddCommand {
  private String serviceIp;
  private String groupName;
}

