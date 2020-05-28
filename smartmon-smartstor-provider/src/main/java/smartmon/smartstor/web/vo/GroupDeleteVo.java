package smartmon.smartstor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Group delete vo")
public class GroupDeleteVo {
  @ApiModelProperty(value = "service ip", position = 1, required = true)
  private String serviceIp;
  @ApiModelProperty(value = "group name", position = 2, required = true)
  private String groupName;
}
