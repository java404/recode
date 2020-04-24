package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PbDataGroupInfo {
  @JsonProperty("initgroup_id")
  private String id;
  @JsonProperty("initgroup_name")
  private String name;
  @JsonProperty("initgroup_info")
  private String groupInfo;
  @JsonProperty("initgroup_nodes")
  private List<PbDataGroupNode> groupNodes;
  @JsonProperty("ext_asm_node_id")
  private String asmNodeId;
  @JsonProperty("ext_asm_node_name")
  private String asmNodeName;
}
