package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "team create model")
public class TeamCreateVo {
  @ApiModelProperty(value = "team name", position = 1)
  private String teamName;
  @ApiModelProperty(value = "team resume", position = 2)
  private String resume;
}
