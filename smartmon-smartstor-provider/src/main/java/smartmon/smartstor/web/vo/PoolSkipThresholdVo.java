package smartmon.smartstor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Pool skip threshold vo")
public class PoolSkipThresholdVo {
  @ApiModelProperty(value = "service ip", position = 1)
	private String serviceIp;
  @ApiModelProperty(value = "pool name", position = 2)
  private String poolName;
  @ApiModelProperty(value = "skip threshhold", position = 3)
  private Integer skipThreshold;
}
