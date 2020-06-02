package smartmon.core.hosts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;
import smartmon.utilities.misc.TargetHost;

@Getter
@Setter
@ApiModel(value = "remote host command")
public class RemoteHostCommand {
  @ApiModelProperty(value = "address", required = true, position = 1)
  private String address;
  @ApiModelProperty(value = "port", required = true, position = 2)
  private Integer port;
  @ApiModelProperty(value = "username", required = true, position = 3)
  private String username;
  @ApiModelProperty(value = "password", required = true, position = 4)
  private String password;

  public TargetHost toTargetHost() {
    return TargetHost.builder(address, port).username(username).password(password).build();
  }
}
