package smartmon.vhe.controller.vo;

import lombok.Data;

@Data
public class NodeDmPathVo {
  private String hctl;
  private String kname;
  private String majorMinor;
  private String dmState;
  private String pathState;
  private String lnode;
  private String lportNum;
  private String lport;
  private String rport;
  private String lportState;
}
