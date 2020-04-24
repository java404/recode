package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PbDataPoolInfos {
  @JsonProperty("pool_infos")
  private List<PbDataPoolInfo> poolInfos;
}
