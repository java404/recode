package smartmon.smartstor.domain.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.domain.model.enums.TransModeEnum;
import smartmon.smartstor.domain.share.Entity;

@Data
@EqualsAndHashCode(callSuper = false)
public class GroupNode extends Entity {
  private String hostId;
  private String hostname;
  private String nodeId;
  private String nodeName;
  private String nodeInfo;
  private TransModeEnum transMode;
  private Integer listenPort;
  private List<String> ibIps;
}
