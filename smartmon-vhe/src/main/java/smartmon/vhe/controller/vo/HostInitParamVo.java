package smartmon.vhe.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Storage Host init Param Vo")
public class HostInitParamVo {
  @ApiModelProperty(value = "hostId", position = 1)
  private String hostId;
  @ApiModelProperty(value = "serviceIp", required = true, position = 2)
  private String serviceIp;
  @ApiModelProperty(value = "system username", required = true, position = 3)
  private String sysUsername;
  @ApiModelProperty(value = "system password", required = true, position = 4)
  private String sysPassword;
  @ApiModelProperty(value = "size", required = true, position = 5)
  private Integer size;
  @ApiModelProperty(value = "idcName", required = true, position = 6)
  private String idcName;
}
