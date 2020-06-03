package smartmon.vhe.controller.vo;

import lombok.Data;

@Data
public class NodeSystemDiskVo {
  private String diskName;
  private String diskRaid;
  private String diskSize;
  private String diskState;
}
