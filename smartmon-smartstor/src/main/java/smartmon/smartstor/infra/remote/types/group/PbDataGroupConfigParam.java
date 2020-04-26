package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PbDataGroupConfigParam {
  @JsonProperty("new_initgroup_name")
  private String newGroupName;
  @JsonProperty("initgroup_info")
  private String groupDecs;
}
