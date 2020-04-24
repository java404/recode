package smartmon.smartstor.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import smartmon.smartstor.domain.model.enums.NodeStatusEnum;
import smartmon.smartstor.domain.model.enums.SysModeEnum;
import smartmon.smartstor.domain.model.enums.TransModeEnum;
import smartmon.smartstor.domain.share.Entity;

@Data
@EqualsAndHashCode(callSuper = false)
public class StorageHost extends Entity {
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

  public String getHostKey() {
    return StringUtils.isNotEmpty(hostId) ? hostId : listenIp;
  }

  public boolean isIos() {
    return SysModeEnum.isIos(sysMode);
  }

  public boolean isBac() {
    return SysModeEnum.isBac(sysMode);
  }

  public boolean isHostConfigured() {
    return StringUtils.isNotEmpty(guid);
  }
}
