package smartmon.smartstor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "lun add vo")
@Data
public class LunAddVo {
  @ApiModelProperty(value = "service ip", position = 1)
  private String serviceIp;
  @ApiModelProperty(value = "diskName", position = 2)
  private String diskName;
  @ApiModelProperty(value = "pool name", position = 3)
  private String poolName;
  @ApiModelProperty(value = "size", position = 4)
  private Integer size;
  @ApiModelProperty(value = "basedisk", position = 5)
  private Boolean basedisk;
  @ApiModelProperty(value = "group name", position = 6)
  private String groupName;
}
