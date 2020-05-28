package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "user create model")
public class NodeConfigCreateVo {
  @ApiModelProperty(value = "name", position = 1)
  private String name;
  @ApiModelProperty(value = "hostname", position = 2)
  private String hostname;
  @ApiModelProperty(value = "data", position = 3)
  private Object data;
}
