package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import smartmon.smartstor.domain.model.GroupNode;
import smartmon.utilities.misc.BeanConverter;

import java.util.List;

@Data
public class PbDataGroupInfo {
  @JsonProperty("initgroup_id")
  private String groupId;
  @JsonProperty("initgroup_name")
  private String groupName;
  @JsonProperty("initgroup_info")
  private String groupInfo;
  @JsonProperty("initgroup_nodes")
  private List<PbDataGroupNode> nodes;
  @JsonProperty("ext_asm_node_id")
  private String asmNodeId;
  @JsonProperty("ext_asm_node_name")
  private String asmNodeName;

  public List<GroupNode> getNodes() {
    return BeanConverter.copy(this.nodes, GroupNode.class);
  }
}
