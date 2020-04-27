package smartmon.smartstor.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.domain.share.Entity;

@Data
@EqualsAndHashCode(callSuper = false)
public class PoolDiskInfo extends Entity {
  private String diskId;
  private Integer diskPart;
  private Long diskSize;
  private String diskDevName;
  private String diskName;
}
