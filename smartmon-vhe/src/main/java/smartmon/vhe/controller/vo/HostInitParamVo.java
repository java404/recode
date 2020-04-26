package smartmon.vhe.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Storage Host init Param Vo")
public class HostInitParamVo {
  @ApiModelProperty(value = "hostId", position = 1)
  private String hostId;
  @ApiModelProperty(value = "serviceIp", position = 2)
  private String serviceIp;
  @ApiModelProperty(value = "size", position = 3)
  private Integer size;
  @ApiModelProperty(value = "idcName", position = 3)
  private String idcName;
}
