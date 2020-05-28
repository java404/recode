package smartmon.smartstor.infra.remote.types.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class PbDataNodeInfo {
  @JsonProperty("node_info")
  @Setter
  private PbDataNodeItem nodeInfo;
  @JsonProperty("ios_service_stat")
  @Getter
  @Setter
  private PbDataServiceStatResponse iosServiceStat;
  @JsonProperty("bac_service_stat")
  @Getter
  @Setter
  private PbDataServiceStatResponse bacServiceStat;

  private static final String HOST_TYPE_STORAGE = "storage"; // 存储节点
  private static final String HOST_TYPE_DATABASE = "database"; // 计算节点
  private static final String HOST_TYPE_MERGE = "merge"; // 混合节点

  public PbDataNodeItem getNodeInfo() {
    if (nodeInfo == null) {
      nodeInfo = new PbDataNodeItem();
      if (iosServiceStat != null && bacServiceStat != null) {
        nodeInfo.setNodeName(iosServiceStat.getIosNodeCfg().getNodeName());
        nodeInfo.setHostId(iosServiceStat.getHostId());
        nodeInfo.setTrsType(iosServiceStat.getIosNodeCfg().getTrsType());
        nodeInfo.setClusterName(iosServiceStat.getIosNodeCfg().getClusterName());
        nodeInfo.setSysMode(HOST_TYPE_MERGE);
      } else if (iosServiceStat != null) {
        nodeInfo.setNodeName(iosServiceStat.getIosNodeCfg().getNodeName());
        nodeInfo.setHostId(iosServiceStat.getHostId());
        nodeInfo.setTrsType(iosServiceStat.getIosNodeCfg().getTrsType());
        nodeInfo.setClusterName(iosServiceStat.getIosNodeCfg().getClusterName());
        nodeInfo.setSysMode(HOST_TYPE_STORAGE);
      } else if (bacServiceStat != null) {
        nodeInfo.setNodeName(bacServiceStat.getBacNodeCfg().getNodeName());
        nodeInfo.setHostId(bacServiceStat.getHostId());
        nodeInfo.setTrsType(bacServiceStat.getBacNodeCfg().getTrsType());
        nodeInfo.setClusterName(bacServiceStat.getBacNodeCfg().getClusterName());
        nodeInfo.setSysMode(HOST_TYPE_DATABASE);
      }
    }
    return nodeInfo;
  }
}
