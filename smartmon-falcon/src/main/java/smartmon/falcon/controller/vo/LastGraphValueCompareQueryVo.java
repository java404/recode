package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "disk health graph model")
public class LastGraphValueCompareQueryVo {
  @ApiModelProperty(value = "tag", position = 1)
  private String tag;
  @ApiModelProperty(value = "counter", position = 2)
  private String counter;
  @ApiModelProperty(value = "hostname", position = 3)
  private String hostname;
}
