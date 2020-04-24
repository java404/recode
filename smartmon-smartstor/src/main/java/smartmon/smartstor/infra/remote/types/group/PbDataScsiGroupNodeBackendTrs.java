package smartmon.smartstor.infra.remote.types.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PbDataScsiGroupNodeBackendTrs {
  @JsonProperty("ibguids")
  private List<String> ibguids;
}
