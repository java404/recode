package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataLunCreateParam {
  @JsonProperty("data_disk_name")
  private String dataDiskName;
  @JsonProperty("basedisk")
  private Boolean baseDisk;
  @JsonProperty("pool_name")
  private String poolName;
  private Integer size;
  @JsonProperty("group_name")
  private String groupName;
}
