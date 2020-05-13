package smartmon.falcon.remote.types.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconTeamCreateParam {
  @JsonProperty("team_name")
  private String teamName;
  private String resume;
}
