package smartmon.smartstor.web.dto;

import lombok.Data;

@Data
public class StorageHostDto {
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
}
