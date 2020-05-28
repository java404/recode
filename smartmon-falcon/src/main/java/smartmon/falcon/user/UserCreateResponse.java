package smartmon.falcon.user;

import lombok.Data;

@Data
public class UserCreateResponse {
  private String id;
  private String name;
  private String sig;
}
