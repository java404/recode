package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataGroupAddNodeParam {
  @JsonProperty("node_name")
  private String nodeName;
  @JsonProperty("node_info")
  private String nodeDesc;
}
