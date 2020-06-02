package smartmon.smartstor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "Pool del vo")
@Data
public class PoolDelVo {
  @ApiModelProperty(value = "service ip", position = 1)
  private String serviceIp;
  @ApiModelProperty(value = "pool name", position = 2)
  private String poolName;
}
