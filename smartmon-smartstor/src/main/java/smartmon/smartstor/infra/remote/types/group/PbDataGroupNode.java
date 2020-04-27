package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import smartmon.smartstor.domain.model.enums.IEnum;
import smartmon.smartstor.domain.model.enums.TransModeEnum;

import java.util.List;

@Data
public class PbDataGroupNode {
  @JsonProperty("node_id")
  private String nodeId;
  @JsonProperty("host_id")
  private String hostId;
  @JsonProperty("node_info")
  private String nodeInfo;
  @JsonProperty("node_name")
  private String nodeName;
  @JsonProperty("ib_ips")
  private List<String> ibIps;
  @JsonProperty("listen_port")
  private Integer listenPort;
  @JsonProperty("host_name")
  private String hostName;
  @JsonProperty("trs_type")
  private Integer transMode;
  @JsonProperty("backend_trs")
  private PbDataGroupNodeBackendTrs groupNodeBackendTrs;

  public TransModeEnum getTransMode() {
    return IEnum.getByCode(TransModeEnum.class, this.transMode);
  }
}
