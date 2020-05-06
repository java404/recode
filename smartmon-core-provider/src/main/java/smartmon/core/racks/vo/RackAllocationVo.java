package smartmon.core.racks.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RackAllocationVo implements Serializable {
  private static final long serialVersionUID = -3223221404651885485L;

  private String idcId;
  private String rackName;
  private Integer rackIndex;
  private String hostUuid;
  private Integer size;
}
