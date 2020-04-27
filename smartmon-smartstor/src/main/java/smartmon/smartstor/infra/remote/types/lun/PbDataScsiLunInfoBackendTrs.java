package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataScsiLunInfoBackendTrs {
  @JsonProperty("fixed_lun")
  private Integer fixedLun;
  @JsonProperty("ext_io_error")
  private Long extIoError;
  @JsonProperty("ext_last_errno")
  private int extLastErrno;
}
