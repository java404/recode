package smartmon.vhe.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class NodeDmDto {
  @JsonProperty("product_id")
  private String pathName;
  private String alias;
  private String kname;
  private String wwid;
  private String devpath;
  @JsonProperty("avail_paths")
  private Long pathTotalCount;
  @JsonProperty("unhealth_paths")
  private Long errorPathCount;
  private JsonNode paths;
  private List<NodeDmPathDto> dmPaths = new ArrayList<>();
  @JsonProperty("vendor_id")
  private String vendorId;
}
