package smartmon.vhe.deployment.command;

import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;
import smartmon.core.hosts.RemoteHostCommand;

@Getter
@Setter
public class SmartstorCommand extends RemoteHostCommand {
  @ApiModelProperty(value = "smartstor installer", required = true, position = 100)
  private String smartstorInstaller;
  @ApiModelProperty(value = "smartstor template", required = true, position = 101)
  private String smartstorTemplate;
}
