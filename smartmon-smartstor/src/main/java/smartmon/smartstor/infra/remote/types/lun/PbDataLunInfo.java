package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import smartmon.smartstor.domain.model.Lun;
import smartmon.smartstor.domain.model.enums.IEnum;
import smartmon.smartstor.domain.model.enums.LunExtStateEnum;

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
  private Long extSize;
  @JsonProperty("asmdisk_info")
  private PbDataAsmDiskInfo asmDiskInfo;
  @JsonProperty("backend_trs")
  private PbDataLunInfoBackendTrs lunInfoBackendTrs;
  @JsonProperty("lun2qostemlates")
  private PbDataLun2QosTemplate lunQosTemplate;
  @JsonProperty("ext_state_onl")
  private Long extStateOnl;
  @JsonProperty("ext_device_path")
  private String extDataDevName;
  @JsonProperty("ext_node_name")
  private String extNodeName;
  @JsonProperty("ext_initgroup_name")
  private String groupName;
  @JsonProperty("ext_backend_res")
  private PbDataLunInfoBackendRes lunInfoBackendRes;
  @JsonProperty("ext_state")
  private Integer extState;
  @JsonProperty("ext_state_level")
  private Integer extStateLevel;

  public Boolean getConfigState() {
    return convertStateByBit(this.stateCfg, 2) != 0;
  }

  private long convertStateByBit(long state, int offset) {
    return state & (1 << offset);
  }

  public String getShowStatus() {
    if (this.lunInfoBackendTrs != null
      && this.lunInfoBackendTrs.getScsiLunInfoBackendTrs() != null
      && this.lunInfoBackendTrs.getScsiLunInfoBackendTrs().getExtIoError() != null
      && this.lunInfoBackendTrs.getScsiLunInfoBackendTrs().getExtIoError() != 0) {
      return "fault";
    } else {
      return IEnum.getByCode(LunExtStateEnum.class, this.extState).getName();
    }
  }

  public String getAsmStatus() {
    return IEnum.getByCode(LunExtStateEnum.class, this.extState).getName();
  }

  public Boolean getActualState() {
    return convertStateByBit(this.extStateOnl, 2) != 0;
  }

  public String getExtSize() {
    return String.valueOf(this.extSize);
  }

}
