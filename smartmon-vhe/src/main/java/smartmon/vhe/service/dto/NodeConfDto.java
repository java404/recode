package smartmon.vhe.service.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class NodeConfDto {
  private String hostname;
  private String name;
  private JsonNode data;
}
