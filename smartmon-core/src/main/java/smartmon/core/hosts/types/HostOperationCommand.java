package smartmon.core.hosts.types;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

@ApiModel(value = "host add model")
@Data
public class HostOperationCommand {
  @ApiModelProperty(value = "manage ip", required = true, position = 1)
  private String manageIp;
  @ApiModelProperty(value = "system username", required = true, position = 2)
  private String sysUsername;
  @ApiModelProperty(value = "system password", required = true, position = 3)
  private String sysPassword;
  @ApiModelProperty(value = "ssh port", required = true, position = 4)
  private Integer sshPort;
  @ApiModelProperty(value = "ipmi address", position = 5)
  private String ipmiAddress;
  @ApiModelProperty(value = "ipmi username", position = 6)
  private String ipmiUsername;
  @ApiModelProperty(value = "ipmi password", position = 7)
  private String ipmiPassword;
}
