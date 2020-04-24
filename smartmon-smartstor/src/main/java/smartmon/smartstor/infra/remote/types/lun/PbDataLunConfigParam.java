package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataLunConfigParam {
  @JsonProperty("initgroup_name")
  private String groupName;
}
