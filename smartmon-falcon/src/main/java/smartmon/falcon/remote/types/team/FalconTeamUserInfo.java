package smartmon.falcon.remote.types.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import smartmon.falcon.remote.types.user.FalconUser;

import java.util.List;

@Data
public class FalconTeamUserInfo {
  private Integer id;
  private String name;
  private String resume;
  @JsonProperty("creator")
  private Integer creatorId;
  private List<FalconUser> users;
  @JsonProperty("creator_name")
  private String creatorName;
}
