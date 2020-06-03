package smartmon.vhe.controller.vo;

import java.util.List;
import lombok.Data;

@Data
public class NodeDetailVo {
  private String hostname;
  private String hostType;
  private Boolean nodeState;
  private String distribution;
  private String version;
  private List<NodeServiceStateVo> serviceStatusInfo;
  private String transMode;
  private String processorModel;
  private Long memoryTotal;
  private NodeSystemDiskVo systemDiskInfo;
  private List<NodeFileSystemVo> fileSystems;
  private List<NodeNicInfoVo> nicInfos;

}
