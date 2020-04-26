package smartmon.smartstor.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.domain.share.Entity;

@Data
@EqualsAndHashCode(callSuper = false)
public class StorageGroup extends Entity {
  private String groupName;
  private String hostUuid;
  private String lunNames;
  private String nodes;
}
