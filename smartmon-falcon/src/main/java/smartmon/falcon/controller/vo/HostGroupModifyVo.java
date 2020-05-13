package smartmon.falcon.controller.vo;

import java.util.Set;
import lombok.Data;

@Data
public class HostGroupModifyVo {
  private String groupId;
  private Set<String> hostNames;
}


