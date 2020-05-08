package smartmon.smartstor.infra.persistence.entity;

import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.domain.model.enums.NodeStatusEnum;
import smartmon.smartstor.domain.model.enums.SysModeEnum;
import smartmon.smartstor.domain.model.enums.TransModeEnum;

@Data
@EqualsAndHashCode(callSuper = false)
public class StorageHostEntity extends BaseEntity {
  private String uuid;
  private String guid;
  private String hostId;
  private String hostname;
  private String listenIp;
  private Integer listenPort;
  private SysModeEnum sysMode;
  private TransModeEnum transMode;
  private String nodeIndex;
  private String nodeName;
  private NodeStatusEnum nodeStatus;
  private String clusterId;
  private String version;
  private Integer versionNum;
  private boolean hostConfigured = false;
  private boolean rackConfigured = false;
  private String clusterName;

  public StorageHostEntity() {
    super();
    this.uuid = UUID.randomUUID().toString();
  }
}
