package smartmon.falcon.remote.types.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import smartmon.falcon.remote.types.user.FalconUser;

import java.util.List;

@Data
public class FalconTeamInfo {
  private FalconTeam team;
  @JsonProperty("creator_name")
  private String creatorName;
  private List<FalconUser> users;
}
