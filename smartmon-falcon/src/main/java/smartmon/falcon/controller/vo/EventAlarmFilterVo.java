package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "event alarm query model")
public class EventAlarmFilterVo {
  @ApiModelProperty(value = "alarm start time", position = 1)
  private Long startTime;
  @ApiModelProperty(value = "alarm end time", position = 2)
  private Long endTime;
  @ApiModelProperty(value = "process status", position = 3)
  private String processStatus;
  @ApiModelProperty(value = "status", position = 4)
  private String status;
  @ApiModelProperty(value = "host name", position = 5)
  private String hostName;
  @ApiModelProperty(value = "priority", position = 6)
  private String priority;

  public Long getEndTime() {
    return this.endTime != null ? this.endTime : System.currentTimeMillis() / 1000;
  }
}
