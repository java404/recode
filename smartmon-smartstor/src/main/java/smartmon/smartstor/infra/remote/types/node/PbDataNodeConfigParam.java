package smartmon.smartstor.infra.remote.types.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataNodeConfigParam {
  @JsonProperty("node_id")
  private Integer nodeId;
  @JsonProperty("trs_type")
  private Integer trsType;
  @JsonProperty("cluster_name")
  private String clusterName;
}
