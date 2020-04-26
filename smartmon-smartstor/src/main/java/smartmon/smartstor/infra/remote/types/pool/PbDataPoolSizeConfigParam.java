package smartmon.smartstor.infra.remote.types.pool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PbDataPoolSizeConfigParam {
  private Long size;
}
