package smartmon.core.racks.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

@Data
@ApiModel(value = "idc add model")
public class IdcAddVo {
  @ApiModelProperty(value = "name", required = true, position = 1)
  private String name;
}
