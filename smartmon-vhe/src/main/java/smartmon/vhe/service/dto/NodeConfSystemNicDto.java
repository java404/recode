package smartmon.vhe.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NodeConfSystemNicDto {
  private String address;
  @JsonProperty("interface")
  private String network;
  private String state;
}
