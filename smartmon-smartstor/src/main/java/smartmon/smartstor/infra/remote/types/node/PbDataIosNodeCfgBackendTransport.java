package smartmon.smartstor.infra.remote.types.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataIosNodeCfgBackendTransport {
  @JsonProperty("ext:pds.backend.trs.smartnvme.ic_bt")
  private PbDataSmartNvme smartNvme;
  @JsonProperty("ext:pds.backend.trs.smartscsi.ic_bt")
  private PbDataSmartScsi smartScsi;
}
