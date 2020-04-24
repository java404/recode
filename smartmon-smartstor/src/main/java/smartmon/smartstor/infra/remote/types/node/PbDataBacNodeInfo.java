package smartmon.smartstor.infra.remote.types.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataBacNodeInfo {
  @JsonProperty("initgroup_id")
  private String initGroupId;
  @JsonProperty("initgroup_name")
  private String initGroupName;
}
