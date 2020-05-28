package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import lombok.Data;

@Data
@ApiModel(value = "host group modify model")
public class HostGroupModifyVo {
  @ApiModelProperty(value = "group id", position = 1)
  private String groupId;
  @ApiModelProperty(value = "hostNames", position = 2)
  private Set<String> hostNames;
}


