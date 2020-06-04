package smartmon.core.racks.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RackAllocateVo {
  private static final int DEFAULT_SIZE = 2;
  private String hostUuid;
  private Integer size;

  public Integer getSize() {
    return size != null && size > 0 ? size : DEFAULT_SIZE;
  }
}
