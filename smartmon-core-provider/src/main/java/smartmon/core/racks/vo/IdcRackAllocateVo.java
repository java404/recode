package smartmon.core.racks.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class IdcRackAllocateVo extends RackAllocateVo {
  private String idcName;
}
