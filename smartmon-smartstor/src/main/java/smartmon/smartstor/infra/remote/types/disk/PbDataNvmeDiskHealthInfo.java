package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataNvmeDiskHealthInfo {
  @JsonProperty("life")
  private Integer nvmeLife;
  @JsonProperty("totallife")
  private Integer nvmeTotallife;
  @JsonProperty("health")
  private String nvmeHealth;
  @JsonProperty("media_status")
  private String nvmeMediaStatus;
}
