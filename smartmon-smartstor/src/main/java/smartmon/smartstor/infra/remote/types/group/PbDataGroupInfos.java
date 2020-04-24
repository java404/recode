package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PbDataGroupInfos {
  @JsonProperty("initgroup_infos")
  private List<PbDataGroupInfo> groupInfos;
}
