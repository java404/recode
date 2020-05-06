package smartmon.falcon.user;

import lombok.Data;

@Data
public class User {
  private Integer id;
  private String name;
  private String cnName;
  private String email;
}
