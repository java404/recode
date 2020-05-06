package smartmon.falcon.remote.types.host;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FalconHost {
  private Integer id;
  private String hostname;
  private String ip;
  @JsonProperty("agent_version")
  private String agentVersion;
  @JsonProperty("plugin_version")
  private String pluginVersion;
  @JsonProperty("maintain_begin")
  private Integer maintainBegin;
  @JsonProperty("maintain_end")
  private Integer maintainEnd;
}
