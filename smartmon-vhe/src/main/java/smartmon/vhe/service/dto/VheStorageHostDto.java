package smartmon.vhe.service.dto;

import lombok.Data;
import smartmon.core.racks.vo.RackAllocationVo;

@Data
public class VheStorageHostDto {
  private String uuid;
  private String guid;
  private String hostId;
  private String hostname;
  private String listenIp;
  private Integer listenPort;
  private String broadcastIp;
  private String sysModeDesc;
  private String transModeDesc;
  private String nodeIndex;
  private String nodeName;
  private String nodeStatusDesc;
  private String clusterId;
  private String clusterName;
  private String version;
  private Integer versionNum;

  private String idcId;
  private Integer size;
  private String rackName;
  private Integer rackIndex;

  public void setRackInfo(RackAllocationVo rackAllocationVo) {
    this.rackName = rackAllocationVo.getRackName();
    this.size = rackAllocationVo.getSize();
    this.rackIndex = rackAllocationVo.getRackIndex();
    this.idcId = rackAllocationVo.getIdcId();
  }
}
