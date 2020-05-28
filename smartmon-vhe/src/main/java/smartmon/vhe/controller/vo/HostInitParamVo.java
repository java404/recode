package smartmon.vhe.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import smartmon.vhe.service.dto.HostInitDto;

@Data
@ApiModel("Storage Host init Param Vo")
public class HostInitParamVo {
  @ApiModelProperty(value = "hostId", position = 1)
  private String hostId;
  @ApiModelProperty(value = "serviceIp", required = true, position = 2)
  private String serviceIp;
  @ApiModelProperty(value = "system username", required = true, position = 3)
  private String sysUsername;
  @ApiModelProperty(value = "system password", required = true, position = 4)
  private String sysPassword;
  @ApiModelProperty(value = "ssh port", required = true, position = 5)
  private Integer sshPort;
  @ApiModelProperty(value = "ipmi address", position = 6)
  private String ipmiAddress;
  @ApiModelProperty(value = "ipmi username", position = 7)
  private String ipmiUsername;
  @ApiModelProperty(value = "ipmi password", position = 8)
  private String ipmiPassword;
  @ApiModelProperty(value = "size", required = true, position = 9)
  private Integer size;
  @ApiModelProperty(value = "idcName", required = true, position = 10)
  private String idcName;

  public static List<HostInitDto> toDtos(List<HostInitParamVo> nodesVo) {
    return CollectionUtils.emptyIfNull(nodesVo).stream()
      .map(HostInitParamVo::toDto)
      .collect(Collectors.toList());
  }

  private static HostInitDto toDto(HostInitParamVo vo) {
    HostInitDto dto = new HostInitDto();
    BeanUtils.copyProperties(vo, dto);
    dto.setListenIp(vo.getServiceIp());
    return dto;
  }
}
