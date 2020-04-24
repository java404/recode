package smartmon.smartstor.infra.remote.types.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PbDataNodeInfos {
  @JsonProperty("node_infos")
  private List<PbDataNodeItem> nodeInfos = Collections.emptyList();
  @JsonProperty("local_host_id")
  private String localHostId;
}
