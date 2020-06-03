package smartmon.vhe.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NodeConfFileSystemDto {
  @JsonProperty("avail")
  private String availSize;
  @JsonProperty("mount")
  private String path;
  @JsonProperty("size")
  private String totalSize;
  @JsonProperty("use_rate")
  private String usageRate;
  @JsonProperty("used")
  private String usedSize;
}
