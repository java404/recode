package smartmon.falcon.remote.types.host;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FalconHosts {
  @JsonProperty("hostgroup")
  private FalconHostGroup hostGroup;
  @JsonProperty("hosts")
  private List<FalconHost> hostList;
}
