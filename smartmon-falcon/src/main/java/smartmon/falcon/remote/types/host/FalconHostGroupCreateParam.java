package smartmon.falcon.remote.types.host;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconHostGroupCreateParam {
  private String name;
  private String note;
}
