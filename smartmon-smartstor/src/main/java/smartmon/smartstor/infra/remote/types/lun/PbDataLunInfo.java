package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataLunInfo {
  @JsonProperty("lun_name")
  private String lunName;
  @JsonProperty("lun_id")
  private String lunId;
  @JsonProperty("lun_type")
  private Integer lunType;
  @JsonProperty("lun_res_id")
  private String lunResId;
  @JsonProperty("state_cfg")
  private Long stateCfg;
  @JsonProperty("initgroup_id")
  private String groupId;
  @JsonProperty("lun_size")
  private Long size;
}
