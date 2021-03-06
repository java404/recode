package smartmon.smartstor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Disk led state vo")
public class DiskLedStateVo {
  @ApiModelProperty(value = "service ip", position = 1)
  private String serviceIp;
  @ApiModelProperty(value = "ces addr", position = 2)
  private String cesAddr;
}
