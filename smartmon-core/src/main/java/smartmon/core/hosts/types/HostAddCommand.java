package smartmon.core.hosts.types;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

@ApiModel(value = "host add model")
@Data
public class HostAddCommand {
  @ApiModelProperty(value = "manage ip", required = true, position = 1)
  private String manageIp;
  @ApiModelProperty(value = "system username", required = true, position = 2)
  private String sysUsername;
  @ApiModelProperty(value = "system password", required = true, position = 3)
  private String sysPassword;
}
