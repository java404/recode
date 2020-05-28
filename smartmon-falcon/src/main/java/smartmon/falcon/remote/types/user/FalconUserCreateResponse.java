package smartmon.falcon.remote.types.user;

import lombok.Data;

@Data
public class FalconUserCreateResponse {
  private String id;
  private String name;
  private String sig;
}
