package smartmon.falcon.remote.types.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconUserUpdateParam {
  @JsonProperty("user_id")
  private Integer id;
  @JsonProperty("cnname")
  private String cnName;
  private String email;
  private String im;
  private String phone;
  private String qq;
}
