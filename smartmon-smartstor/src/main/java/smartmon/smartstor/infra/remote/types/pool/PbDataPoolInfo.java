package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import smartmon.smartstor.domain.model.PoolDiskInfo;
import smartmon.utilities.misc.BeanConverter;

@Data
public class PbDataPoolInfo {
  @JsonProperty("pool_id")
  private String poolId;
  @JsonProperty("pool_name")
  private String poolName;
  @JsonProperty("disk")
  private PbDataPoolDiskInfo diskInfo;
  @JsonProperty("extent")
  private Long extent;
  @JsonProperty("bucket")
  private Long bucket;
  @JsonProperty("sippet")
  private Long sippet;
  @JsonProperty("dirty_thresh")
  private PbDataPoolDirtyThresh dirtyThresh;
  @JsonProperty("sync_level")
  private Integer syncLevel;
  @JsonProperty("skip_thresh")
  private Integer skipThresh;
  @JsonProperty("is_variable")
  private Boolean isVariable;
  @JsonProperty("ext_actual_state")
  private Boolean actualState;
  @JsonProperty("ext_lht")
  private Long lastHeartbeatTime;
  @JsonProperty("ext_pool_export_info")
  private PbDataPoolExportInfo exportInfo;
  @JsonProperty("ext_cache_model")
  private Integer extPoolInfoPoolCacheModel;
  @JsonProperty("ext_pmt_size")
  private Long extPoolInfoPoolPmtSize;
  @JsonProperty("ext_total_cachehit_rate")
  private Double extPoolInfoTotalCacheHitRate;
  @JsonProperty("ext_read_cachehit_rate")
  private Double extPoolInfoReadCacheHitRate;
  @JsonProperty("ext_write_cachehit_rate")
  private Double extPoolInfoWriteCacheHitRate;

  public PoolDiskInfo getDiskInfo() {
    return BeanConverter.copy(this.diskInfo, PoolDiskInfo.class);
  }
}
