package smartmon.falcon.remote.types.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconUserCreateParam {
  private String name;
  private String password;
  @JsonProperty("cnname")
  private String cnName;
  private String email;
}
