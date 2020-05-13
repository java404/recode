package smartmon.falcon.remote.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconApiToken {
  private String name;
  private String sig;
}
