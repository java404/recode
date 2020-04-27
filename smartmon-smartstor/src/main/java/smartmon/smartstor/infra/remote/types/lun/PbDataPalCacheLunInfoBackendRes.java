package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataPalCacheLunInfoBackendRes {
  @JsonProperty("cache_size")
  private Long cacheSize;
  @JsonProperty("data_dev_name")
  private String dataDevName;
  @JsonProperty("data_disk_name")
  private String dataDiskName;
  @JsonProperty("cache_dev_name")
  private String cacheDevName;
  @JsonProperty("cache_disk_name")
  private String cacheDiskName;
  @JsonProperty("pool_name")
  private String poolName;
}
