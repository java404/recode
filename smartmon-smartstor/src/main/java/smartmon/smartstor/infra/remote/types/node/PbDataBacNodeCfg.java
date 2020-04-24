package smartmon.smartstor.infra.remote.types.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PbDataBacNodeCfg {
  @JsonProperty("cluster_name")
  private String clusterName;
  @JsonProperty("node_name")
  private String nodeName;
  @JsonProperty("trs_type")
  private Integer trsType;
  @JsonProperty("ext_ib_ips")
  private List<String> extIbIps;
}
