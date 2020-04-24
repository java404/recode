package smartmon.smartstor.infra.remote.types.node;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import smartmon.smartstor.domain.model.enums.IEnum;
import smartmon.smartstor.domain.model.enums.NodeStatusEnum;
import smartmon.smartstor.domain.model.enums.SysModeEnum;
import smartmon.smartstor.domain.model.enums.TransModeEnum;

@Data
public class PbDataNodeItem {
  @JsonProperty("node_id")
  private String nodeId;
  @JsonProperty("host_id")
  private String hostId;
  @JsonProperty("ib_ips")
  private List<String> ibIps;
  @JsonProperty("err_ib_ips")
  private List<String> errIbIps;
  @JsonProperty("listen_port")
  private Integer listenPort;
  @JsonProperty("node_name")
  private String nodeName;
  @JsonProperty("cluster_name")
  private String clusterName;
  @JsonProperty("node_type")
  private Integer nodeType;
  @JsonProperty("trs_type")
  private Integer trsType;
  @JsonProperty("host_name")
  private String hostname;
  @JsonProperty("bac")
  private PbDataBacNodeInfo bac;
  @JsonProperty("ios")
  private PbDataIosNodeInfo ios;
  @JsonProperty("actual_state")
  private Boolean actualState;

  @JsonProperty("listen_Ip")
  private String listenIp;
  @JsonProperty("sysMode")
  private String sysMode;
  @JsonProperty("trans_mode")
  private String transMode;
  private String platform;
  @JsonProperty("broadcast_ip")
  private String broadcastIp;
  @JsonProperty("last_broadcast_time")
  private Long lastBroadcastTime;
  @JsonProperty("node_uuid")
  private String nodeUuid;
  @JsonProperty("node_index")
  private String nodeIndex;
  @JsonProperty("node_status")
  private Integer nodeStatus;

  public String getListenIp() {
    return StringUtils.isNotEmpty(this.listenIp) ? this.listenIp : this.getHostId();
  }

  public SysModeEnum getSysMode() {
    return StringUtils.isNotEmpty(this.sysMode) ? IEnum.getByName(SysModeEnum.class, this.sysMode) :
      IEnum.getByCode(SysModeEnum.class, this.getNodeType());
  }

  public TransModeEnum getTransMode() {
    return StringUtils.isNotEmpty(this.transMode) ? IEnum.getByName(TransModeEnum.class, this.transMode) :
      IEnum.getByCode(TransModeEnum.class, this.getTrsType());
  }

  public NodeStatusEnum getNodeStatus() {
    if (this.nodeStatus == null) {
      if (this.getBac() == null && this.getIos() == null) {
        return NodeStatusEnum.UNCONFIGURED;
      } else {
        return this.getActualState() ? NodeStatusEnum.CONFIGURED : NodeStatusEnum.MISSING;
      }
    } else {
      return IEnum.getByCode(NodeStatusEnum.class, this.nodeStatus);
    }
  }

  public String getNodeUuid() {
    return StringUtils.isNotEmpty(this.nodeUuid) ? this.nodeUuid : this.getNodeId();
  }
}
