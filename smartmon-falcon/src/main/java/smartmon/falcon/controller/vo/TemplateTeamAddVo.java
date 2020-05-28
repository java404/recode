package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "template team add model")
public class TemplateTeamAddVo {
  @ApiModelProperty(value = "template id", position = 1)
  private Integer templateId;
  @ApiModelProperty(value = "team name", position = 2)
  private String teamName;
}
