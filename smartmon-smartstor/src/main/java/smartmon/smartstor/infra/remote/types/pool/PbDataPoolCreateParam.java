package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PbDataPoolCreateParam {
  @JsonProperty("disk_name")
  private String diskName;
  @JsonProperty("is_variable")
  private Boolean isVariable;
  @JsonProperty("extent")
  private Long extent;
  @JsonProperty("bucket")
  private Long bucket;
  @JsonProperty("sippet")
  private Long sippet;
}
