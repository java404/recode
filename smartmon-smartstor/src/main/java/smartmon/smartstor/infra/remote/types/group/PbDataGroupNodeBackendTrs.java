package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataGroupNodeBackendTrs {
  @JsonProperty("ext:pds.backend.trs.smartnvme.ign_bt")
  private PbDataNvmeGroupNodeBackendTrs nvmeGroupNodeBackendTrs;
  @JsonProperty("ext:pds.backend.trs.smartscsi.ign_bt")
  private PbDataScsiGroupNodeBackendTrs scsiGroupNodeBackendTrs;
}
