package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataAsmDiskInfo {
  private Integer dsknum;
  private String grpname;
  private String fgname;
  @JsonProperty("lun_id")
  private String lunId;
  @JsonProperty("ext_lht")
  private Long lastHeartbeatTime;
  @JsonProperty("ext_dskname")
  private String extDiskName;
  @JsonProperty("ext_path")
  private String extPath;
  @JsonProperty("ext_mode_status")
  private String extModeStatus;
  @JsonProperty("ext_state")
  private String extState;
}
