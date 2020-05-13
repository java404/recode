package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import smartmon.falcon.alarm.model.EventNoteStatusEnum;

@Data
@ApiModel(value = "event note handle model")
public class EventNoteCreateVo {
  @ApiModelProperty(value = "event note")
  private String note;
  @ApiModelProperty(value = "event status")
  private EventNoteStatusEnum status;
}
