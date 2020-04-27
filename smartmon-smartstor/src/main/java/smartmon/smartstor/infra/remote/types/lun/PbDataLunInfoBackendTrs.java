package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataLunInfoBackendTrs {
  @JsonProperty("ext:pds.backend.trs.smartnvme.li_bt")
  private PbDataNvmeLunInfoBackendTrs nvmeLunInfoBackendTrs;
  @JsonProperty("ext:pds.backend.trs.smartscsi.li_bt")
  private PbDataScsiLunInfoBackendTrs scsiLunInfoBackendTrs;
}
