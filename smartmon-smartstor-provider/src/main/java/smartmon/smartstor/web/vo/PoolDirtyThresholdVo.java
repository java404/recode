package smartmon.smartstor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Pool dirty thresh")
public class PoolDirtyThresholdVo {
  @ApiModelProperty(value = "service ip", position = 1)
  private String serviceIp;
  @ApiModelProperty(value = "pool name", position = 2)
	private String poolName;
  @ApiModelProperty(value = "thresh lower", position = 3)
  private Integer threshLower;
  @ApiModelProperty(value = "thresh upper", position = 4)
  private Integer threshUpper;
}
