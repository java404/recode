package smartmon.falcon.remote.types.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FalconTemplateInfo {
  private FalconTemplate template;
  @JsonProperty("parent_name")
  private String parentName;
}
