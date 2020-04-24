package smartmon.smartstor.infra.remote.types.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataIosNodeCfg {
  @JsonProperty("cluster_name")
  private String clusterName;
  @JsonProperty("node_name")
  private String nodeName;
  @JsonProperty("trs_type")
  private Integer trsType;
  @JsonProperty("last_target")
  private Integer lastTarget;
  @JsonProperty("backend_trs")
  private PbDataIosNodeCfgBackendTransport backendTrs;
}
