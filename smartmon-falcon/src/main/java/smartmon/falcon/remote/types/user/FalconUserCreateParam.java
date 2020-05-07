package smartmon.falcon.remote.types.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconUserCreateParam {
  private String name;
  private String password;
  private String cnname;
  private String email;
}
