package smartmon.falcon.template;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HostGroupTemplate extends Template {
  private Integer groupId;
  private String groupName;
}
