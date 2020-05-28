package smartmon.smartstor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Group add luns vo")
public class GroupLunAddVo {
  @ApiModelProperty(value = "serviceIp", position = 1, required = true)
  private String serviceIp;
  @ApiModelProperty(value = "lunName", position = 2, required = true)
  private String lunName;
}
