package smartmon.core.hostinfo;

import io.swagger.annotations.ApiModel;

import lombok.Getter;
import lombok.Setter;
import smartmon.core.hosts.RemoteHostCommand;

@Getter
@Setter
@ApiModel(value = "scan network command")
public class ScanNetworkCommand extends RemoteHostCommand {
}
