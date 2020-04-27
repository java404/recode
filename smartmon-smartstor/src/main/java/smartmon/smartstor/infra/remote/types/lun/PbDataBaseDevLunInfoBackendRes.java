package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataBaseDevLunInfoBackendRes {
  @JsonProperty("dev_name")
  private String devName;
}
