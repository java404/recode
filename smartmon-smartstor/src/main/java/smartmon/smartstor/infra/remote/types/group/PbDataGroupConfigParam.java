package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataGroupConfigParam {
  @JsonProperty("new_initgroup_name")
  private String newGroupName;
  @JsonProperty("initgroup_info")
  private String groupDecs;
}
