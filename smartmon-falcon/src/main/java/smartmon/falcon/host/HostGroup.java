package smartmon.falcon.host;

import lombok.Data;

@Data
public class HostGroup {
  private Integer id;
  private String groupName;
  private String note;
  private String createUser;
}
