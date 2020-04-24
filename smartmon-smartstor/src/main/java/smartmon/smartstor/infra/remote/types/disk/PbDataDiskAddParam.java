package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PbDataDiskAddParam {
  @JsonProperty("dev_name")
  private String devName;
  @JsonProperty("partition_count")
  private Integer partitionCount;
  @JsonProperty("disk_type")
  private String diskType;
}
