package smartmon.vhe.service.dto;

import java.util.List;
import lombok.Data;

@Data
public class NodeDetailDto {
  private String hostname;
  private String hostType;
  private Boolean nodeState;
  private String distribution;
  private String version;
  private List<NodeServiceStateDto> serviceStatus;
  private String transMode;
  private String processorModel;
  private Long memoryTotal;
  private NodeConfSystemDiskDto systemDisk;
  private List<NodeConfFileSystemDto> fileSystem;
  private List<NodeConfSystemNicDto> nics;

}
