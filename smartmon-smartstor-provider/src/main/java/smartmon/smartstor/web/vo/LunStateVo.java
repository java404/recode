package smartmon.smartstor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "lun state")
public class LunStateVo {
  @ApiModelProperty(value = "service ip", position = 1)
  private String serviceIp;
  @ApiModelProperty(value = "lun name", position = 2)
  private String lunName;
  @ApiModelProperty(value = "online", position = 3)
  private Boolean online;

}

