package smartmon.smartstor.interfaces.web.representation.dto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = false)
public class GroupDto {
  private String serviceIp;
  private String nodeName;
  private String hostName;
  private String groupName;
  private Long lunCount;
  private Long nodeCount;
  private List<GroupNodeDto> groupNodes;

  public Long getNodeSize() {
    if (CollectionUtils.isEmpty(groupNodes)) {
      return 0L;
    }
    return (long)groupNodes.size();
  }
}
