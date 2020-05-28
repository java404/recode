package smartmon.smartstor.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PoolDto extends SimplePoolDto {
  private Integer syncLevel;
  private Integer skipThresh;
  private Integer dirtyThreshLower;
  private Integer dirtyThreshUpper;
  private String dirtyLu;
  private String dirtyNp;
  private String validNp;
  private Long pmtSize;
  private Long freeSize;
  private String cachePmtFree;
  private String poolCacheMode;
  private String disk;
  private Double extPoolInfoReadCacheHitRate;
  private Double extPoolInfoWriteCacheHitRate;
  private Double extPoolInfoTotalCacheHitRate;

  private Boolean isVariable;
  private Long exportError;
  private Long cacheSize;

  public static PoolDto empty() {
    return new PoolDto();
  }
}
