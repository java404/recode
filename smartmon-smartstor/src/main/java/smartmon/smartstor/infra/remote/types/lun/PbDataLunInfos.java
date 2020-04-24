package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PbDataLunInfos {
  @JsonProperty("lun_infos")
  private List<PbDataLunInfo> lunInfos;
}
