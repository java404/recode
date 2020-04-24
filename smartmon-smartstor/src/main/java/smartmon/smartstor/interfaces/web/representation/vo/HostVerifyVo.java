package smartmon.smartstor.interfaces.web.representation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import smartmon.smartstor.app.command.HostVerifyCommand;

@Data
@ApiModel(value = "host verify model")
public class HostVerifyVo {
  @ApiModelProperty(value = "host", position = 1)
  private List<HostVerifyVo.Host> hosts;

  @Data
  @ApiModel(value = "host model")
  private static class Host {
    @ApiModelProperty(value = "service ip", position = 1)
    private String serviceIp;
    @ApiModelProperty(value = "service ip", position = 2)
    private String listenIp;
    @ApiModelProperty(value = "host id", position = 3)
    private String hostId;
  }

  public HostVerifyCommand toHostVerifyCommand() {
    HostVerifyCommand command = new HostVerifyCommand();
    List<HostVerifyCommand.Host> hosts = getHosts().stream()
      .map(host -> new HostVerifyCommand.Host(host.getServiceIp(), host.getListenIp(), host.getHostId()))
      .collect(Collectors.toList());
    command.setHosts(hosts);
    return command;
  }
}
