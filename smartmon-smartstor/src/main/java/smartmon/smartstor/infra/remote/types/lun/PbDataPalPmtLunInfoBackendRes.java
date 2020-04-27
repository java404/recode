package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataPalPmtLunInfoBackendRes {
  @JsonProperty("data_dev_name")
  private String dataDevName;
  @JsonProperty("data_disk_name")
  private String dataDiskName;
  @JsonProperty("pool_name")
  private String poolName;
}
