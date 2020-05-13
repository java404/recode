package smartmon.falcon.user.command;

import lombok.Data;

@Data
public class TeamCreateCommand {
  private String teamName;
  private String resume;
}
