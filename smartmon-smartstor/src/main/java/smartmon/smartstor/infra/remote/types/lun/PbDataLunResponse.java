package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataLunResponse {
  @JsonProperty("lun_info")
  private PbDataLunInfo lunInfo;
}
