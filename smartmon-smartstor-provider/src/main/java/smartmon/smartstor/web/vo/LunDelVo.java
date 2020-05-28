package smartmon.smartstor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "Delete lun")
@Data
public class LunDelVo {
  @ApiModelProperty(value = "service ip", position = 1)
  private String serviceIp;
  @ApiModelProperty(value = "lun name", position = 2)
  private String lunName;
  @ApiModelProperty(value = "is lvvote", position = 3)
  private Boolean isLvvote;
  @ApiModelProperty(value = "ignore asmstatus", position = 4)
	private Boolean ignoreAsmstatus;

}
