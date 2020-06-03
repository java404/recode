package smartmon.vhe.deployment.command;

import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmartstorDeployCommand extends SmartstorCommand {
  @ApiModelProperty(value = "ipmi address", position = 5)
  private String ipmiAddress;
  @ApiModelProperty(value = "ipmi username", position = 6)
  private String ipmiUsername;
  @ApiModelProperty(value = "ipmi password", position = 7)
  private String ipmiPassword;
  @ApiModelProperty(value = "network parameters", position = 102)
  private String networkParameters;
  @ApiModelProperty(value = "opensm parameters", position = 103)
  private String opensmParameters;
}
