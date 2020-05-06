package smartmon.falcon.remote.types.host;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconHostGroupUpdateParam {
  private Integer id;
  @JsonProperty("grp_name")
  private String name;
  private String note;
}
