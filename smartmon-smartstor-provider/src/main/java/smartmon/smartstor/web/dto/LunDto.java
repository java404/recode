package smartmon.smartstor.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LunDto extends SimpleLunDto {
  private String dataDisk;
  private String cacheDisk;
  private Boolean configState;
  private Long lastHeartbeatTime;
  private String extCacheDiskName;

  private String lunId;
  private String extScsiId;
  private String extDataActualState;
  private String size;
  private String extCacheActualStates;
  private Integer extPalCacheCacheModel;
  private String extNodeName;
  private String extPalCacheId;
  private String devName;
  private Long cacheSize;
  private String groupUuid;
  private String diskName;
  private String extCacheDevNames;
  private String qosTemplateId;

  private String configAsmStatus;
  private String groupNodeUuids;

  private String exportLunName;
  private String exportT10DevId;
  private String exportThreadsNum;
  private String exportThreadsPoolType;
  private Integer exportIoError;
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

  public static LunDto empty() {
    return new LunDto();
  }
}
