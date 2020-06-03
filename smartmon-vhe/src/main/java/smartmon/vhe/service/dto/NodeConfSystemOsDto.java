package smartmon.vhe.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class NodeConfSystemOsDto {
  private List<String> cpu = new ArrayList<>();
  private Long mem;
  private List<NodeConfSystemNicDto> nic = new ArrayList<>();
  @JsonProperty("ver")
  private String version;

}
