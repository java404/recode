package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import smartmon.falcon.graph.model.MergerTypeEnum;

@Data
@ApiModel(value = "general monitor graph model")
public class GeneralMonitorGraphQueryVo {
  @ApiModelProperty(value = "tag", position = 1)
  private String tag;
  @ApiModelProperty(value = "counters", position = 2)
  private List<String> counters;
  @ApiModelProperty(value = "hostUuid", position = 3)
  private String hostUuid;
  @ApiModelProperty(value = "startTime", position = 4)
  private Long startTime;
  @ApiModelProperty(value = "endTime", position = 5)
  private Long endTime;
  @ApiModelProperty(value = "mergerType", position = 6)
  private MergerTypeEnum mergerType;
  @ApiModelProperty(value = "step", position = 7)
  private Integer step;
  @ApiModelProperty(value = "range", position = 8)
  private Long range;
}
