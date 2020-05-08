package smartmon.smartstor.interfaces.web.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "host init model")
public class HostInitVo {
  @ApiModelProperty(value = "guid", position = 1)
  private String guid;
  @ApiModelProperty(value = "serviceIp", position = 2)
  private String listenIp;
  @ApiModelProperty(value = "hostId", position = 3)
  private String hostId;
}
