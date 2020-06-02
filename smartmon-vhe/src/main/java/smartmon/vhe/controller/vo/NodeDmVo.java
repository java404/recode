package smartmon.vhe.controller.vo;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class NodeDmVo {
  private String pathName;
  private String alias;
  private String kname;
  private String wwid;
  private String devpath;
  private Long pathTotalCount;
  private Long errorPathCount;
  private List<NodeDmPathVo> dmPaths = new ArrayList<>();
}
