package smartmon.smartstor.infra.remote.types.pool;

import lombok.Data;

@Data
public class PbDataPoolDirtyThresh {
  private Long lower;
  private Long upper;
}
