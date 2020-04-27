package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataLunInfoBackendRes {
  @JsonProperty("ext:pds.backend.lun.basedev.li_br")
  private PbDataBaseDevLunInfoBackendRes baseDevLunInfoBackendRes;
  @JsonProperty("ext:pds.backend.lun.basedisk.li_br")
  private PbDataBaseDiskLunInfoBackendRes baseDiskLunInfoBackendRes;
  @JsonProperty("ext:pds.backend.lun.palcache.li_br")
  private PbDataPalCacheLunInfoBackendRes palCacheLunInfoBackendRes;
  @JsonProperty("ext:pds.backend.lun.palpmt.li_br")
  private PbDataPalPmtLunInfoBackendRes palPmtLunInfoBackendRes;
  @JsonProperty("ext:pds.backend.lun.palraw.li_br")
  private PbDataPalRawLunInfoBackendRes palRawLunInfoBackendRes;
}
