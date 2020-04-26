package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PbDataGroupAddParam {
  @JsonProperty("initgroup_name")
  private String groupName;
  @JsonProperty("initGroup_info")
  private String groupDesc;
}
