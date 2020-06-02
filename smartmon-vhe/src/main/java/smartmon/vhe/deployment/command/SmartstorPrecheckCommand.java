package smartmon.vhe.deployment.command;

import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;
import smartmon.core.hosts.RemoteHostCommand;

@Getter
@Setter
public class SmartstorPrecheckCommand extends RemoteHostCommand {
  @ApiModelProperty(value = "smartstor installer", required = true, position = 5)
  private String smartstorInstaller;
  @ApiModelProperty(value = "smartstor template", required = true, position = 6)
  private String smartstorTemplate;
}
