package smartmon.falcon.user.command;

import lombok.Data;

@Data
public class UserUpdateCommand {
  private Integer id;
  private String cnName;
  private String email;
}
