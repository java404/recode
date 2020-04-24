package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataPoolInfo {
  @JsonProperty("pool_id")
  private String poolId;
  @JsonProperty("pool_name")
  private String poolName;
  @JsonProperty("disk")
  private PbDataPoolDiskInfo poolDiskInfo;
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
  private Long lht;
  @JsonProperty("ext_pool_export_info")
  private PbDataPoolExportInfo exportInfo;
  @JsonProperty("ext_cache_model")
  private Integer cacheModel;
  @JsonProperty("ext_pmt_size")
  private Long pmtSize;
  @JsonProperty("ext_total_cachehit_rate")
  private Double total;
  @JsonProperty("ext_read_cachehit_rate")
  private Double read;
  @JsonProperty("ext_write_cachehit_rate")
  private Double write;
}
