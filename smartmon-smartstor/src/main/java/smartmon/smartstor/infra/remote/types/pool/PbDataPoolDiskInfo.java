package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataPoolDiskInfo {
  @JsonProperty("disk_id")
  private String diskId;
  @JsonProperty("disk_part")
  private Integer diskPart;
  @JsonProperty("paldisk_id")
  private String palDiskId;
  @JsonProperty("replace_disk_id")
  private String replaceDiskId;
  @JsonProperty("replace_paldisk_id")
  private String replacePalDiskId;
  @JsonProperty("ext_dev_name")
  private String diskDevName;
  @JsonProperty("ext_disk_name")
  private String diskName;
  @JsonProperty("ext_size")
  private Long diskSize;
  @JsonProperty("ext_replace_dev_name")
  private String extReplaceDevName;
  @JsonProperty("ext_replace_disk_name")
  private String extReplaceDiskName;
}
