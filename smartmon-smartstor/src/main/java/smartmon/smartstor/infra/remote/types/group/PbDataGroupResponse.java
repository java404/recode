package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataGroupResponse {
  @JsonProperty("initgroup_info")
  private PbDataGroupInfo groupInfo;
}
