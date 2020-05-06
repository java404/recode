package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataLunInfoBackendRes {
  @JsonProperty("ext:pds.backend.lun.basedev.li_br")
  private PbDataBaseDevLunInfoBackendRes baseDevLiBr;
  @JsonProperty("ext:pds.backend.lun.basedisk.li_br")
  private PbDataBaseDiskLunInfoBackendRes baseDiskLiBr;
  @JsonProperty("ext:pds.backend.lun.palcache.li_br")
  private PbDataPalCacheLunInfoBackendRes palCacheLiBr;
  @JsonProperty("ext:pds.backend.lun.palpmt.li_br")
  private PbDataPalPmtLunInfoBackendRes palPmtLiBr;
  @JsonProperty("ext:pds.backend.lun.palraw.li_br")
  private PbDataPalRawLunInfoBackendRes palRawLiBr;

  /** Get data disk name. */
  public String getDataDiskName() {
    if (baseDevLiBr != null) {
      return null;
    } else if (baseDiskLiBr != null) {
      return baseDiskLiBr.getDataDiskName();
    } else if (palCacheLiBr != null) {
      return palCacheLiBr.getDataDiskName();
    } else if (palPmtLiBr != null) {
      return palPmtLiBr.getDataDiskName();
    } else if (palRawLiBr != null) {
      return palRawLiBr.getDataDiskName();
    }
    return null;
  }

  /** Get data dev name. */
  public String getDataDevName() {
    if (baseDevLiBr != null) {
      return baseDevLiBr.getDevName();
    } else if (baseDiskLiBr != null) {
      return baseDiskLiBr.getDataDevName();
    } else if (palCacheLiBr != null) {
      return palCacheLiBr.getDataDevName();
    } else if (palPmtLiBr != null) {
      return palPmtLiBr.getDataDevName();
    } else if (palRawLiBr != null) {
      return palRawLiBr.getDataDevName();
    }
    return null;
  }
}
