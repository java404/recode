package smartmon.core.racks.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(value = "host add to rack model")
@Data
@EqualsAndHashCode(callSuper = true)
public class IdcRackAllocateVo extends RackAllocateVo {
  @ApiModelProperty(value = "idc name", required = true, position = 3)
  private String idcName;
}
