package smartmon.smartstor.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.domain.share.Entity;

@Data
@EqualsAndHashCode(callSuper = false)
public class Lun extends Entity {
  private String hostUuid;
  private String lunName;
  private Boolean configState;
  private Long lastHeartbeatTime;
  private String extCacheDiskName;
  private Boolean actualState;
  private String lunId;
  private String extScsiid;
  private String extDataActualState;
  private String extSize;
  private String extCacheActualStates;
  private Integer extPalcacheCacheModel;
  private String extNodeName;
  private String extPalcacheId;
  private String extDataDevName;
  private Long extCacheSize;
  private Integer lunType;
  private String groupUuid;
  private String extDataDiskName;
  private String extCacheDevNames;
  private String qosTemplateId;
  private String asmStatus;
  private String configAsmStatus;
  private String showStatus;
  private String groupNodeUuids;
  private String groupName;
  private String exportLunName;
  private String exportT10DevId;
  private String exportThreadsNum;
  private String exportThreadsPoolType;
  private Integer exportIOError;
  private Integer exportLastErrno;
  private String exportFilename;
  private Long exportSizeMb;
  private String exporteds;
  private String exportGroupNames;
  private Long statsUse;
  private Long statsAveq;
  private Long statsRmerge;
  private Long statsRsect;
  private Long statsWio;
  private Long statsWuse;
  private Long statsRuse;
  private Long statsRio;
  private Long statsRunning;
  private Long statsWsect;
}
