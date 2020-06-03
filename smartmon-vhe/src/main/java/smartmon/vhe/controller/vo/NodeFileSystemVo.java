package smartmon.vhe.controller.vo;

import lombok.Data;

@Data
public class NodeFileSystemVo {
  private String availSize;
  private String path;
  private String totalSize;
  private String usageRate;
  private String usedSize;
}
