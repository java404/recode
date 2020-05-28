package smartmon.falcon.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "disk health graph model")
public class LastGraphValueCompareQueryVo {
  @ApiModelProperty(value = "tag", position = 1)
  private String tag;
  @ApiModelProperty(value = "counter", position = 2)
  private String counter;
  @ApiModelProperty(value = "hostUuid", position = 3)
  private String hostUuid;

  public static LastGraphValueCompareQueryVo newVo(String tag, String counter, String hostUuid) {
    LastGraphValueCompareQueryVo vo = new LastGraphValueCompareQueryVo();
    vo.setTag(tag);
    vo.setCounter(counter);
    vo.setHostUuid(hostUuid);
    return vo;
  }
}
