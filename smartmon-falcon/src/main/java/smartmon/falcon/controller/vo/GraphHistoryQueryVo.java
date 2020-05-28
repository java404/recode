package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import lombok.Data;
import smartmon.falcon.graph.model.MergerTypeEnum;

@Data
@ApiModel(value = "graph history model")
public class GraphHistoryQueryVo {
  @ApiModelProperty(value = "hosts", position = 1)
  private Set<String> hosts;
  @ApiModelProperty(value = "counters", position = 2)
  private Set<String> counters;
  @ApiModelProperty(value = "mergerType", position = 3)
  private MergerTypeEnum mergerType;
  @ApiModelProperty(value = "startTime", position = 4)
  private Long startTime;
  @ApiModelProperty(value = "endTime", position = 5)
  private Long endTime;
  @ApiModelProperty(value = "step", position = 6)
  private Integer step;
  @ApiModelProperty(value = "range", position = 7)
  private Long range;
}
