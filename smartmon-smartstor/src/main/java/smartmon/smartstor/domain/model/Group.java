package smartmon.smartstor.domain.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.domain.share.Entity;

@Data
@EqualsAndHashCode(callSuper = false)
public class Group extends Entity {
  private String hostUuid;
  private String groupId;
  private String groupName;
  private String groupInfo;
  private List<GroupNode> nodes;
}
