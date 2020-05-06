package smartmon.falcon.remote.types.host;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FalconHostGroup {
  private Integer id;
  @JsonProperty("grp_name")
  private String groupName;
  @JsonProperty("create_user")
  private String createUser;
  private String note;
}
