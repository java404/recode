package smartmon.smartstor.infra.remote.types.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataServiceStatResponse {
  @JsonProperty("node_id")
  private String nodeId;
  @JsonProperty("host_id")
  private String hostId;
  @JsonProperty("is_ready")
  private Boolean isReady;
  @JsonProperty("node_type")
  private Integer nodeType;
  @JsonProperty("ios_nodecfg")
  private PbDataIosNodeCfg iosNodeCfg;
  @JsonProperty("bac_nodecfg")
  private PbDataBacNodeCfg bacNodeCfg;


}
