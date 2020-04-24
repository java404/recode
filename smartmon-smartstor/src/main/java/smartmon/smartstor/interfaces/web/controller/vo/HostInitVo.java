package smartmon.smartstor.interfaces.web.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import smartmon.smartstor.domain.model.enums.IEnum;
import smartmon.smartstor.domain.model.enums.NodeStatusEnum;
import smartmon.smartstor.domain.model.enums.SysModeEnum;
import smartmon.smartstor.domain.model.enums.TransModeEnum;

@Data
@ApiModel(value = "host init model")
public class HostInitVo {
  @ApiModelProperty(value = "guid", position = 1)
  private String guid;
  @ApiModelProperty(value = "serviceIp", position = 1)
  private String listenIp;
  @ApiModelProperty(value = "hostId", position = 1)
  private String hostId;

  private Integer sysMode;
  @ApiModelProperty(value = "transMode", position = 6)
  private Integer transMode;
  @ApiModelProperty(value = "nodeStatus", position = 10)
  private Integer nodeStatus;

  public SysModeEnum getSysMode() {
    return IEnum.getByCode(SysModeEnum.class, this.sysMode);
  }

  public TransModeEnum getTransMode() {
    return IEnum.getByCode(TransModeEnum.class, this.transMode);
  }

  public NodeStatusEnum getNodeStatus() {
    return IEnum.getByCode(NodeStatusEnum.class, this.nodeStatus);
  }
}
