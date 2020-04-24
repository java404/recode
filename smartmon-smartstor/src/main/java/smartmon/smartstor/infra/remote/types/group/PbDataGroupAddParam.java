package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataGroupAddParam {
  @JsonProperty("initgroup_name")
  private String groupName;
  @JsonProperty("initGroup_info")
  private String groupDesc;
}
