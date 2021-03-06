package smartmon.smartstor.interfaces.web.representation.dto;

import lombok.Data;
import smartmon.smartstor.domain.model.enums.NodeStatusEnum;
import smartmon.smartstor.domain.model.enums.SysModeEnum;
import smartmon.smartstor.domain.model.enums.TransModeEnum;

@Data
public class StorageHostDto {
  private String uuid;
  private String guid;
  private String hostId;
  private String hostname;
  private String listenIp;
  private Integer listenPort;
  private String broadcastIp;
  private SysModeEnum sysMode;
  private TransModeEnum transMode;
  private String nodeIndex;
  private String nodeName;
  private NodeStatusEnum nodeStatus;
  private String clusterId;
  private String clusterName;
  private String version;
  private Integer versionNum;
}
