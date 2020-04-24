package smartmon.smartstor.interfaces.web.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

@Data
@ApiModel(value = "disk add model")
public class DiskAddVo {
  @ApiModelProperty(value = "service ip", position = 1)
  private String serviceIp;
  @ApiModelProperty(value = "device path", position = 2)
  private String devName;
  @ApiModelProperty(value = "partition count", position = 3)
  private Integer partitionCount;
  @ApiModelProperty(value = "disk type, hdd/ssd supported", position = 4)
  private String diskType;
}
