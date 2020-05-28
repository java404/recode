package smartmon.smartstor.app.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupLunAddCommand {
  private String serviceIp;
  private String lunName;
  private String groupName;
}
