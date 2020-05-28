package smartmon.smartstor.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.domain.model.enums.PoolCacheModeEnum;
import smartmon.smartstor.domain.share.Entity;

@Data
@EqualsAndHashCode(callSuper = false)
public class Pool extends Entity {
  private String hostUuid;
  private String poolId;
  private String poolName;
  private Long lastHeartbeatTime;
  private Boolean actualState;
  private Integer syncLevel;
  private Integer skipThresh;
  private Integer dirtyThreshLower;
  private Integer dirtyThreshUpper;
  private Long extPoolInfoPoolPmtSize;
  private Double extPoolInfoReadCacheHitRate;
  private Double extPoolInfoWriteCacheHitRate;
  private Double extPoolInfoTotalCacheHitRate;
  private PoolCacheModeEnum extPoolInfoPoolCacheModel;
  private Boolean isVariable;
  private Integer exportState;
  private String exportStateStr;
  private Boolean exportIsVariable;
  private Long exportExtent;
  private Long exportBucket;
  private Long exportSippet;
  private String exportDevNames;
  private Long exportSize;
  private Long exportMaxSize;
  private Long exportLowerThresh;
  private Double exportPLowerThresh;
  private Long exportUpperThresh;
  private Double exportPUpperThresh;
  private Long exportValid;
  private Double exportPValid;
  private Long exportDirty;
  private Double exportPDirty;
  private Long exportError;
  private PoolDiskInfo diskInfo;
}
