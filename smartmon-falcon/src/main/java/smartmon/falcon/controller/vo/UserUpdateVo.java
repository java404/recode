package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "user update model")
public class UserUpdateVo {
  @ApiModelProperty(value = "user cnName", position = 2)
  private String cnName;
  @ApiModelProperty(value = "user email", position = 3)
  private String email;
}
