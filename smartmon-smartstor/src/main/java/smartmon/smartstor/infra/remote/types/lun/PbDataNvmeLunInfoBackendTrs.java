package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PbDataNvmeLunInfoBackendTrs {
  @JsonProperty("fixed_port")
  private Integer fixedPort;
  @JsonProperty("ext_trans_addr")
  private List<PbDataLunIpAddr> extTransAddr;
  @JsonProperty("ext_hostnqn")
  private String extHostNqn;
  @JsonProperty("ext_subnqn")
  private String extSubNqn;
}
