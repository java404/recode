package smartmon.smartstor.infra.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import smartmon.smartstor.domain.model.enums.NodeStatusEnum;
import smartmon.smartstor.domain.model.enums.SysModeEnum;
import smartmon.smartstor.domain.model.enums.TransModeEnum;
import smartmon.smartstor.interfaces.web.representation.dto.StorageHostDto;

import java.util.UUID;

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

  public StorageHostEntity convert(StorageHostDto hostDto) {
    StorageHostEntity entity = new StorageHostEntity();
    try {
      BeanUtils.copyProperties(hostDto, entity);
    } catch (Exception e) {
      return null;
    }
    return entity;
  }
}
