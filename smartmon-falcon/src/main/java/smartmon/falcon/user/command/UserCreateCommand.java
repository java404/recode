package smartmon.falcon.user.command;

import lombok.Data;

@Data
public class UserCreateCommand {
  private String name;
  private String password;
  private String cnName;
  private String email;

  public String getPassword() {
    return this.password != null ? this.password : "123456";
  }
}
