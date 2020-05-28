package smartmon.smartstor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "lun actice state vo")
@Data
public class LunActiveStateVo {
  @ApiModelProperty(value = "service ip", position = 1)
  private String serviceIp;
  @ApiModelProperty(value = "lunName", position = 2)
  private String lunName;
  @ApiModelProperty(value = "active", position = 3)
  private boolean active;
}
