package smartmon.falcon.remote.types.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FalconUser {
  private Integer id;
  private String name;
  @JsonProperty("cnname")
  private String cnName;
  private String email;
  private String phone;
  private String im;
  private String qq;
  private Integer role;
}
