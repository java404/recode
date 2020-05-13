package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "team update model")
public class TeamUpdateVo {
  @ApiModelProperty(value = "team resume", position = 1)
  private String resume;
}
