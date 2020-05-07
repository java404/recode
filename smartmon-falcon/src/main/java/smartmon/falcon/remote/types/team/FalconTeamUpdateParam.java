package smartmon.falcon.remote.types.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import smartmon.falcon.remote.types.user.FalconUser;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconTeamUpdateParam {
  @JsonProperty("team_id")
  private Integer id;
  private String resume;
  private String name;
  private List<FalconUser> users;

}
