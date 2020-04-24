package smartmon.core.racks.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "host add to rack model")
public class RackAllocateVo {
  @ApiModelProperty(value = "host uuid", required = true, position = 1)
  private String hostUuid;
  @ApiModelProperty(value = "size", required = true, position = 2)
  private Integer size;
}
