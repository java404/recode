package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PbDataGroupAddNodeParam {
  @JsonProperty("node_name")
  private String nodeName;
  @JsonProperty("node_info")
  private String nodeDesc;
}
