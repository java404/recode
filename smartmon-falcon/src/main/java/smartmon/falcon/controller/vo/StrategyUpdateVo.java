package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "strategy update model")
public class StrategyUpdateVo {
  @ApiModelProperty(value = "id", position = 1)
  private Long id;
  @ApiModelProperty(value = "pause", position = 2)
  private Integer pause;
  @ApiModelProperty(value = "op", position = 3)
  private String op;
  @ApiModelProperty(value = "rightValue", position = 4)
  private String rightValue;
  @ApiModelProperty(value = "maxStep", position = 5)
  private Long maxStep;
  @ApiModelProperty(value = "priority", position = 6)
  private Integer priority;
  @ApiModelProperty(value = "note", position = 7)
  private String note;
}
