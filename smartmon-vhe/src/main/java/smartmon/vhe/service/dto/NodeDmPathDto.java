package smartmon.vhe.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NodeDmPathDto {
  private String hctl;
  private String kname;
  private String devno;
  @JsonProperty("dm_status")
  private String dmState;
  @JsonProperty("path_status")
  private String pathState;
  private String lnode;
  @JsonProperty("lport_num")
  private String lportNum;
  private String lport;
  private String rport;
  @JsonProperty("lport_state")
  private String lportState;
}
