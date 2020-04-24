package smartmon.smartstor.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.domain.model.enums.DiskTypeEnum;
import smartmon.smartstor.domain.share.Entity;

@Data
@EqualsAndHashCode(callSuper = false)
public class Disk extends Entity {
  private String hostUuid;
  private String diskName;
  private String devName;
  private DiskTypeEnum diskType;
  private Long size;
  private Boolean actualState;
  private Long lastHeartbeatTime;
  private Long extFreeSize;
  private String extDiskpartToLunNames;
  private String extDiskpartToPoolNames;
  private Long nvmeLife;
  private Long nvmeTotallife;
  private String nvmeHealth;
  private String nvmeMediaStatus;
  private String headerUuid;
  private String raidInfo;
  private String diskParts;
}
