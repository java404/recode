package smartmon.smartstor.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Group node add vo")
public class GroupNodeAddVo {
  @ApiModelProperty(value = "service ip", position = 1, required = true)
  private String serviceIp;
  @ApiModelProperty(value = "node name", position = 2, required = true)
  private String nodeName;
}
