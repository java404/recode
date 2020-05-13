package smartmon.falcon.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "user create model")
public class UserCreateVo {
  @ApiModelProperty(value = "user name", position = 1)
  private String name;
  @ApiModelProperty(value = "user password", position = 2)
  private String password;
  @ApiModelProperty(value = "user cnName", position = 3)
  private String cnName;
  @ApiModelProperty(value = "user email", position = 4)
  private String email;
}
