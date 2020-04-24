package smartmon.core.racks.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RackAllocation implements Serializable {
  private static final long serialVersionUID = -3223221404651885485L;

  private String idcId;
  private String rackName;
  private Integer rackIndex;
  private String hostUuid;
  private Integer size;
}
